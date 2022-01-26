package hotsixturtles.tupli.api;

import hotsixturtles.tupli.dto.response.ErrorResponse;
import hotsixturtles.tupli.entity.User;
import hotsixturtles.tupli.entity.auth.ProviderType;
import hotsixturtles.tupli.entity.auth.RoleType;
import hotsixturtles.tupli.repository.UserRepository;
import hotsixturtles.tupli.service.FileService;
import hotsixturtles.tupli.service.UserService;
import hotsixturtles.tupli.service.token.JwtTokenProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "회원 가입 API")
public class UserApiController {

    private final UserRepository userRepository;

    private final UserService userService;
    private final FileService fileService;
    private final PasswordEncoder passwordEncoder;

    private final MessageSource messageSource;  // 국제화 이용시

    private final JwtTokenProvider jwtTokenProvider;
    /**
     * 회원 가입
     * @param request
     * @param bindingResult
     * @return
     * 반환 코드 : 201/ 404
     */
    @PostMapping("/account/signup")
    @ApiOperation(value = "회원가입", notes = "회원가입 진행. 성공여부에 따라 'id' 또는 'error.same.id' 값을 반환한다.")
    public ResponseEntity signup(@ApiParam(value = "회원정보") @Validated @RequestBody CreateUserRequest request,
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

    @DeleteMapping("/account/withdraw")
    public ResponseEntity<?> signout(@RequestBody Map<String, String> userInfo){
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
    public ResponseEntity<?> login(@RequestBody Map<String, String> userInfo) {
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
     * 프로필 편집
     * @param file : 프로필 이미지 파일, ProfileImage
     * @param userInfo : {email, nickcname, introduction}
     * @param request
     * @return
     * @throws IOException
     * 반환 코드 : 200, 404
     */
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestPart(value = "image", required = false) MultipartFile file,
                                           @RequestBody Map<String, String> userInfo,
                                           HttpServletRequest request) throws IOException {


        // jwt 유효 확인 + 정보 빼기
        String token = request.getHeader("AUTH");//.replaceFirst("Bearer ", "");
        if(!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);

        // 이미지 업로드 예시
        String image = "";
        try {
        if (file != null) {
            image = fileService.imageUploadGCS(file, userSeq);
            System.out.println("이미지 업로드 완료!");
        }
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.wrong", null, LocaleContextHolder.getLocale())));  // 임시코드
        }
        System.out.println("이미지 업로드 MMM = " + image);

        userService.updateProfile(userInfo);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }


}
