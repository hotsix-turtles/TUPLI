package hotsixturtles.tupli.api;

import hotsixturtles.tupli.dto.response.ErrorResponse;
import hotsixturtles.tupli.entity.User;
import hotsixturtles.tupli.entity.auth.ProviderType;
import hotsixturtles.tupli.entity.auth.RoleType;
import hotsixturtles.tupli.entity.likes.UserLikes;
import hotsixturtles.tupli.repository.UserRepository;
import hotsixturtles.tupli.service.FileService;
import hotsixturtles.tupli.service.UserService;
import hotsixturtles.tupli.service.token.JwtTokenProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.Response;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "회원 관리 API")
public class UserApiController {

    private final UserRepository userRepository;

    private final UserService userService;
    private final FileService fileService;
    private final PasswordEncoder passwordEncoder;

    private final MessageSource messageSource;

    private final JwtTokenProvider jwtTokenProvider;
    /**
     * 회원 가입
     * @param request
     * @param bindingResult
     * @return
     * 반환 코드 : 201/ 404
     */
    @PostMapping("/account/signup")
    @ApiOperation(value = "회원가입", notes = "실패 시 404'이미 있는 아이디입니다' 반환, 성공 시 201 반환")
    public ResponseEntity signup(@ApiParam(value = "email, username, nickname, password를 받습니다.") @Validated @RequestBody CreateUserRequest request,
                                 BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(bindingResult.getAllErrors().get(0).getDefaultMessage()));
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setNickname(request.getNickname());
        user.setProviderType(ProviderType.LOCAL);
        user.setEmailVerifiedYn("N");
        user.setCreatedAt(LocalDateTime.now());
        user.setModifiedAt(LocalDateTime.now());
        user.setRoleType(RoleType.USER);
        user.setIs_vip("N");

        try {
            Long id = userService.join(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(null);
        }
        catch (IllegalStateException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource
                            .getMessage("error.same.id", null, LocaleContextHolder.getLocale())));
        }
    }

    @Data
    static class CreateUserRequest {
        @Size(min=3, max=128, message = "{error.size.username}")
        private String username;
        @Size(min=3, max=128, message = "{error.size.email}")
//        @Email(message = "{error.format.email}")
//        @Email(message = "{email.notempty}")
        private String email;
        private String nickname;
        @Size(min=3, max=128, message = "{error.size.password}")
//        @Length(min=3, max=128, message = "비밀번호 길이 불일치")
        private String password;
        @Size(max = 64) String userId;
        @Size(max = 1) String emailVerifiedYn;
        @Size(max = 512) String profileImageUrl;
        ProviderType providerType;
        RoleType roleType;
        LocalDateTime createdAt;
        LocalDateTime modifiedAt;
    }

    /**
     *
     * @param userInfo
     * @return
     */
    @DeleteMapping("/account/withdraw")
    @ApiOperation(value = "회원탈퇴", notes = "실행 후 204 반환")
    public ResponseEntity<?> signout(@ApiParam(value = "userInfo를 받습니다.")@RequestBody Map<String, String> userInfo){
        userService.deleteUser(Long.parseLong(userInfo.get("userId")));
        SecurityContextHolder.clearContext();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    /**
     * 로그인 JWT 발급
     * @param userInfo {email, password}
     * @return
     */
    @PostMapping("/account/login")
    @ApiOperation(value = "로그인", notes = "실패 시 404'존재하지 않은 유저입니다' 또는 404'잘못된 비밀번호입니다' 반환, 성공 시 token 반환")
    public ResponseEntity<?> login(@ApiParam(value = "email, password를 받습니다.") @RequestBody Map<String, String> userInfo) {
        User user = userRepository.findByEmail(userInfo.get("email"));
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(messageSource.getMessage("error.none.user", null, LocaleContextHolder.getLocale())));
        }

        if (!passwordEncoder.matches(userInfo.get("password"), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(messageSource.getMessage("error.wrong.password", null, LocaleContextHolder.getLocale())));
        }
        String token = jwtTokenProvider.createToken(user.getUsername(), user.getUserSeq());

        return ResponseEntity.ok(new LoginUserResponse(token));
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public class LoginUserResponse {
        private String token;
        public LoginUserResponse(String accessToken) {
            this.token = accessToken;
        }
    }

    /**
     * 프로필 내용 변경
     * @param file
     * @param email
     * @param nickname
     * @param request
     * @return
     * @throws IOException
     * 반환 코드 : 200, 404
     */
    @PutMapping("/profile")
    @ApiOperation(value = "프로필 내용 변경", notes = "회원정보가 안맞을 시 404'유효하지 않은 토큰입니다' 반환, " +
            "프로필 사진 업로드에 문제가 있을 경우 404'잘못된 값입니다' 반환, 성공 시 200 반환")
    public ResponseEntity<?> updateProfile(@RequestPart(value = "image", required = false) MultipartFile file,
                                           @RequestPart(value = "email", required = false) String email,
                                            @RequestPart(value = "nickname", required = false) String nickname,
                                           HttpServletRequest request) throws IOException {
        // jwt 유효 확인 + 정보 빼기
        String token = request.getHeader("Authorization");
        if(!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource
                            .getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);

        // 이미지 업로드 예시
        String image = "";
        try {
        if (file != null) {
            image = fileService.imageUploadGCS(file, userSeq);
        }
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource
                            .getMessage("error.wrong", null, LocaleContextHolder.getLocale())));
        }

        userService.updateProfile(userSeq, email, nickname, image);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    /**
     * 팔로우 하기
     * 내 로그인 정보와 유저의 아이디 받아옴. 내 회원id가 그 회원id를 좋아한다고 해야함. 반환되는건 없어도 된다.
     */
    @PostMapping("/follow")
    @ApiOperation(value = "팔로우 하기", notes = "회원정보가 안맞을 시 404'유효하지 않은 토큰입니다' 반환, " +
            "정상 실행 시 200, body에 null 반환")
    public ResponseEntity<?> followUser(
            @ApiParam(value = "Authroization, userName 으로 입력된다.")
            @RequestBody Map<String, String> info) {
        String token = info.get("Authroization");
        String otherUserName = info.get("userName");
        // 내 로그인 정보가 맞으면 진행, 틀리면 로그인정보가 틀립니다 하고 빠꾸
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.
                            getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        // 내 로그인정보로 id 찾아냄.
        Long userSeq = jwtTokenProvider.getUserSeq(token);
        // 상대 userName 으로 고유 id 찾아냄.
        Long otherUserSeq = userService.getUserByUserName(otherUserName).getUserSeq();
        // user_likes 테이블에 기록.
        userService.follow(userSeq, otherUserSeq);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    /**
     * 해당 유저를 언팔로우 하기
     * @param info
     * @return
     */
    @DeleteMapping("/follow")
    @ApiOperation(value = "언팔로우 하기", notes = "회원정보가 안맞을 시 404'유효하지 않은 토큰입니다' 반환, " +
            "정상 실행 시 200, body에 null 반환")
    public ResponseEntity<?> unfollowUser(
            @ApiParam(value = "Authroization, userName 으로 입력된다.")
            @RequestBody Map<String, String> info) {
        String token = info.get("Authroization");
        String otherUserName = info.get("userName");
        // 내 로그인 정보가 맞으면 진행, 틀리면 로그인정보가 틀립니다 하고 빠꾸
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.
                            getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);
        Long otherUserSeq = userService.getUserByUserName(otherUserName).getUserSeq();
        userService.unfollow(userSeq, otherUserSeq);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    /**
     * 해당 유저의 팔로워(이 유저를 팔로우 한 사람) 보기
     * @param info
     * @return
     */
    @GetMapping("/follow")
    public ResponseEntity<?> getFollowers(
            @ApiParam(value = "Authroization, userName 으로 입력된다.")
            @RequestBody Map<String, String> info) {
        String token = info.get("Authroization");
        String otherUserName = info.get("userName");
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.
                            getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);
        Long otherUserSeq = userService.getUserByUserName(otherUserName).getUserSeq();
        // user_likes 테이블에 to_user 에 otherUserSeq 이 들어간 리스트 반환하면 될듯
        // 22.02.02 한길 - 미완성... 잘 될지 안될지 모름.

        List<UserLikes> followerList = userService.getFollowers(otherUserSeq);
        return ResponseEntity.status(HttpStatus.OK).body(followerList);
    }

}
