package hotsixturtles.tupli.api;

import hotsixturtles.tupli.dto.UserDto;
import hotsixturtles.tupli.dto.UserProfileDto;
import hotsixturtles.tupli.dto.response.ErrorResponse;
import hotsixturtles.tupli.dto.simple.SimpleBadgeDto;
import hotsixturtles.tupli.dto.simple.SimpleUpdateProfileDto;
import hotsixturtles.tupli.dto.simple.SimpleUserDto;
import hotsixturtles.tupli.entity.Badge;
import hotsixturtles.tupli.entity.Board;
import hotsixturtles.tupli.entity.User;
import hotsixturtles.tupli.entity.UserBadge;
import hotsixturtles.tupli.entity.auth.ProviderType;
import hotsixturtles.tupli.entity.auth.RoleType;
import hotsixturtles.tupli.entity.likes.UserDislikes;
import hotsixturtles.tupli.entity.likes.UserLikes;
import hotsixturtles.tupli.entity.meta.UserInfo;
import hotsixturtles.tupli.repository.UserInfoRepository;
import hotsixturtles.tupli.repository.UserRepository;
import hotsixturtles.tupli.service.*;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "회원 관리 API")
public class UserApiController {

    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;

    private final UserService userService;
    private final BadgeService badgeService;
    private final FileService fileService;
    private final PasswordEncoder passwordEncoder;
    private final MailSendService mailSendService;

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
//        @Size(min=3, max=128, message = "{error.size.username}")
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
//        @Size(max = 1) String emailVerifiedYn;
//        @Size(max = 512) String profileImageUrl;
//        ProviderType providerType;
//        RoleType roleType;
        LocalDateTime createdAt;
        LocalDateTime modifiedAt;
    }

    /**
     * 회원 탈퇴
     * @param token
     * @return
     */
    @DeleteMapping("/account/withdraw")
    @ApiOperation(value = "회원탈퇴", notes = "실행 후 204 반환")
    public ResponseEntity<?> signout(@RequestHeader(value = "Authorization") String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);

        userService.deleteUser(userSeq);

        SecurityContextHolder.clearContext();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    /**
     * 비밀번호 변경
     * @param token
     * @param request
     * @param bindingResult
     * @return
     */
    @PutMapping("/account/password")
    public ResponseEntity passwordChange(@RequestHeader(value = "Authorization") String token,
                                         @Validated @RequestBody passwordChangeRequest request,
                                         BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(bindingResult.getAllErrors().get(0).getDefaultMessage()));
        }

        // 유저 인증 및 확인
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        User user = jwtTokenProvider.getUser(token);

        // 기존 비밀번호 체크
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(messageSource.getMessage("error.wrong.password", null, LocaleContextHolder.getLocale())));
        }

        // 비밀번호 변경
        try {
            userService.changePassword(user, request.getPasswordChange());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        catch (IllegalStateException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource
                            .getMessage("error", null, LocaleContextHolder.getLocale())));
        }
    }

    @Data
    static class passwordChangeRequest {
        @NotNull
        private String password;
        @Size(min=3, max=128, message = "{error.size.password}")
        private String passwordChange;
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

        UserInfo nowUserInfo = userInfoRepository.findOneByUserSeq(user.getUserSeq());
        nowUserInfo.setLoginCount(nowUserInfo.getLoginCount() + 1L);

        List<UserBadge> userbadges = badgeService.getBadgeList(user.getUserSeq());
        List<Long> badges = badgeService.getUserBadgeSeq(userbadges);
        List<Badge> badgeResult = new ArrayList<>();
        badgeResult = badgeService.checkLoginNum(user.getUserSeq(), badges);

        if(nowUserInfo.getDailyLoginYN().equals("N")) {
            nowUserInfo.setDailyLoginYN("Y");
            nowUserInfo.setDailyCheck(nowUserInfo.getDailyCheck() + 1L);
            badgeResult.addAll(badgeService.checkDaily(user.getUserSeq(), badges));
        }
        userInfoRepository.save(nowUserInfo);

        String token = jwtTokenProvider.createToken(user.getUsername(), user.getUserSeq());

        if(badgeResult == null || badgeResult.size() == 0) return ResponseEntity.ok(new LoginUserResponse(token, null));
        List<SimpleBadgeDto> badgeDtoResult = badgeResult.stream().map(b -> new SimpleBadgeDto(b)).collect(Collectors.toList());
        return ResponseEntity.ok(new LoginUserResponse(token, badgeDtoResult));
    }

    /**
     * 로그인 성공 시 유저정보 받아감.
     * @param token
     * @return
     */
    @GetMapping("/account/userInfo")
    public ResponseEntity getMyProfile(@RequestHeader(value = "Authorization") String token) {
        try {
            User user = jwtTokenProvider.getUser(token);
            return ResponseEntity.ok().body(new UserProfileDto(user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    /**
     * token 유효 확인, 자동 로그아웃 등에 활용
     * @param token
     * @return
     */
    @GetMapping("/account/tokenvalidate")
    public ResponseEntity<?> checkTokenValidate(@RequestHeader(value = "Authorization") String token){
        // 인증 확인후 돌리기
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.
                            getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        return ResponseEntity.ok().body(null);
    }

    /**
     * 임시비밀번호 발송
     * @param userSeq
     * @return
     */
    @PutMapping("/account/passwordFind/{userSeq}")
    public ResponseEntity passwordFind(@PathVariable("userSeq") Long userSeq) {
        // 임시 비밀번호
        mailSendService.sendTmpPassword(userSeq);
        return ResponseEntity.ok().body(null);
    }


    /**
     * 타인 프로필 정보 간단히 긁어오기 함수
     * @param userSeq
     * @return 
     */
    @GetMapping("/account/profile/{userSeq}")
    public ResponseEntity getOtherProfile(@PathVariable("userSeq") Long userSeq) {
        User user = userRepository.findByUserSeq(userSeq);
        return ResponseEntity.ok().body(new UserDto(user));
    }


    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public class LoginUserResponse {
        private String token;
        private List<SimpleBadgeDto> badges;
        public LoginUserResponse(String accessToken, List<SimpleBadgeDto> badges) {
            this.token = accessToken;
            this.badges = badges;
        }
    }

    /**
     * 프로필 내용 변경
     * @param file
     * @param nickname
     * @param request
     * @return SimpleUpdateProfileDto {content : introduction, nickname, image }
     * @throws IOException
     * 반환 코드 : 200, 404
     */
    @PutMapping("/profile")
    @ApiOperation(value = "프로필 내용 변경", notes = "회원정보가 안맞을 시 404'유효하지 않은 토큰입니다' 반환, " +
            "프로필 사진 업로드에 문제가 있을 경우 404'잘못된 값입니다' 반환, 성공 시 200 반환")
    public ResponseEntity<?> updateProfile(@RequestPart(value = "image", required = false) MultipartFile file,
                                           @RequestPart(value = "introduction", required = false) String introduction,
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

        userService.updateProfile(userSeq, introduction, nickname, image);

        SimpleUpdateProfileDto updateProfileDto = new SimpleUpdateProfileDto(introduction, nickname, image);
        return ResponseEntity.status(HttpStatus.OK).body(updateProfileDto);
    }

    
    /**
     * 팔로우 하기
     * 내 로그인 정보와 유저의 아이디 받아옴. 내 회원id가 그 회원id를 좋아한다고 해야함. 반환되는건 뱃지반환
     */
    @PostMapping("/account/follow/{userSeq}")
    @ApiOperation(value = "팔로우 하기", notes = "회원정보가 안맞을 시 404'유효하지 않은 토큰입니다' 반환, " +
            "정상 실행 시 200 반환")
    public ResponseEntity<?> followUser(
            @ApiParam(value = "Authroization 으로 입력된다.")
            @RequestHeader(value = "Authorization") String token,
            @ApiParam(value = "팔로우 할 상대의 userSeq.")
            @PathVariable("userSeq") Long otherUserSeq) {
        // 내 로그인 정보가 맞으면 진행, 틀리면 로그인정보가 틀립니다 하고 빠꾸
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.
                            getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        // 내 로그인정보로 id 찾아냄.
        Long userSeq = jwtTokenProvider.getUserSeq(token);
        UserLikes userLikes = userService.getFollow(userSeq, otherUserSeq);
        // 팔로우 안되어있으면 팔로우 실행
        if (userLikes == null) {
            userService.follow(userSeq, otherUserSeq);

            List<UserBadge> userbadges = badgeService.getBadgeList(userSeq);
            List<Long> badges = badgeService.getUserBadgeSeq(userbadges);
            List<Badge> badgeResult = new ArrayList<>();

            badgeResult.addAll(badgeService.checkFollowees(userSeq, badges));

            if(badgeResult.size() == 0) badgeResult = null;
            List<SimpleBadgeDto> badgeDtoResult = badgeResult.stream().map(b -> new SimpleBadgeDto(b)).collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(badgeDtoResult);
        }
        // 이미 팔로우 되어있음
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    /**
     * 언팔로우 하기
     */
    @DeleteMapping("/account/follow/{userSeq}")
    @ApiOperation(value = "언팔로우 하기", notes = "회원정보가 안맞을 시 404'유효하지 않은 토큰입니다' 반환, " +
            "정상 실행 시 200 반환")
    public ResponseEntity<?> unfollowUser(
            @ApiParam(value = "Authroization 으로 입력된다.")
            @RequestHeader(value = "Authorization") String token,
            @ApiParam(value = "언팔로우 할 상대의 userSeq.")
            @PathVariable("userSeq") Long otherUserSeq) {
        // 내 로그인 정보가 맞으면 진행, 틀리면 로그인정보가 틀립니다 하고 빠꾸
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.
                            getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);
        UserLikes userLikes = userService.getFollow(userSeq, otherUserSeq);
        if (userLikes != null) {
            userService.unfollow(userSeq, otherUserSeq);
            return ResponseEntity.status(HttpStatus.OK).body("unfollowed");
        }
        // 이미 언팔로우 되어있음
        return ResponseEntity.status(HttpStatus.OK).body("already followed");
    }

    /**
     * 팔로우 했는지 여부를 반환하는 함수
     */
    @GetMapping("/account/follow/{userSeq}")
    @ApiOperation(value = "팔로우 했는지 여부 반환", notes = "회원정보가 안맞을 시 404'유효하지 않은 토큰입니다' 반환, " +
            "정상 실행 시 200 반환")
    public ResponseEntity<?> getFollowUser(
            @ApiParam(value = "Authroization 으로 입력된다.")
            @RequestHeader(value = "Authorization") String token,
            @ApiParam(value = "팔로우 할 상대의 userSeq.")
            @PathVariable("userSeq") Long otherUserSeq) {
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.
                            getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);
        UserLikes userLikes = userService.getFollow(userSeq, otherUserSeq);
        if (userLikes == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);  // 안했음
        }
        return ResponseEntity.status(HttpStatus.OK).body("ok");  // 했음
    }


    /**
     * 해당 유저를 싫어요 하기
     * @return
     */
    @PostMapping("/account/dislike/{userSeq}")
    @ApiOperation(value = "싫어요 하기", notes = "회원정보가 안맞을 시 404'유효하지 않은 토큰입니다' 반환, " +
            "정상 실행 시 200, body에 null 반환")
    public ResponseEntity<?> dislikeUser(
            @ApiParam(value = "Authroization 으로 입력된다.")
            @RequestHeader(value = "Authorization") String token,
            @ApiParam(value = "싫어요 할 상대의 userSeq.")
            @PathVariable("userSeq") Long otherUserSeq) {
        // 내 로그인 정보가 맞으면 진행, 틀리면 로그인정보가 틀립니다 하고 빠꾸
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.
                            getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);
        UserDislikes userDislikes = userService.getDislike(userSeq, otherUserSeq);
        // 싫어요 안되어있으면 싫어요 실행
        if (userDislikes == null) {
            userService.dislike(userSeq, otherUserSeq);
            return ResponseEntity.status(HttpStatus.OK).body("checked dislike");
        }
        // 이미 싫어요 되어있음
        return ResponseEntity.status(HttpStatus.OK).body("already checked dislike");

    }

    /**
     * 해당 유저의 싫어요 체크를 해제하기
     * @return
     */
    @DeleteMapping("/account/dislike/{userSeq}")
    @ApiOperation(value = "싫어요 해제 하기", notes = "회원정보가 안맞을 시 404'유효하지 않은 토큰입니다' 반환, " +
            "정상 실행 시 200, body에 null 반환")
    public ResponseEntity<?> undoDislikeUser(
            @ApiParam(value = "Authroization 으로 입력된다.")
            @RequestHeader(value = "Authorization") String token,
            @ApiParam(value = "싫어요를 해제할 상대의 userSeq.")
            @PathVariable("userSeq") Long otherUserSeq) {
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.
                            getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);
        UserDislikes userDislikes = userService.getDislike(userSeq, otherUserSeq);
        if (userDislikes != null) {
            userService.undoDislike(userSeq, otherUserSeq);
            return ResponseEntity.status(HttpStatus.OK).body("unchecked dislike");
        }
        // 이미 싫어요 해제되어있음.
        return ResponseEntity.status(HttpStatus.OK).body("already unchecked dislike");
    }

    /**
     * 싫어요 했는지 여부를 반환하는 함수
     */
    @GetMapping("/account/dislike/{userSeq}")
    @ApiOperation(value = "싫어요 했는지 여부 반환", notes = "회원정보가 안맞을 시 404'유효하지 않은 토큰입니다' 반환, " +
            "정상 실행 시 200 반환")
    public ResponseEntity<?> getDislikeUser(
            @ApiParam(value = "Authroization 으로 입력된다.")
            @RequestHeader(value = "Authorization") String token,
            @ApiParam(value = "싫어요 한 상대의 userSeq.")
            @PathVariable("userSeq") Long otherUserSeq) {
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.
                            getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);
        UserDislikes userDislikes = userService.getDislike(userSeq, otherUserSeq);
        if (userDislikes == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);  // 안했음
        }
        return ResponseEntity.status(HttpStatus.OK).body("ok");  // 했음
    }

    /**
     * 해당 유저의 팔로워(이 유저를 팔로우 한 사람) 보기
     */
    @GetMapping("/profile/followers/{userSeq}")
    public ResponseEntity<?> getFollowers(
            @ApiParam(value = "auth token")
            @RequestHeader(value = "Authorization") String token,
            @ApiParam(value = "path로 userSeq가 입력된다.")
            @PathVariable("userSeq") Long userSeq) {
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage
                            ("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        // user_likes 테이블에 to_user 에 otherUserSeq 이 들어간 리스트 반환하면 될듯
        // 22.02.02 한길 - 미완성... 잘 될지 안될지 모름.
        // 민구. 네 이대로는 안될겁니다. 이건 중간 테이블이고 가공을 해서 보내주셔야죠. 거의 다 오셨습니다.
        // 반환은 Entity가 아니라 DTO로 해주세요. 이거 하다보면 눈치 채실겁니당b
        List<UserLikes> followerList = userService.getFollowers(userSeq);
        return ResponseEntity.status(HttpStatus.OK).body(followerList);
    }



    /**
     * 해당 유저의 팔로워(이 유저를 팔로우 한 사람) 수 보기
     */
    @GetMapping("/profile/followers/{userSeq}/count")
    public ResponseEntity<?> getFollowers(
            @ApiParam(value = "path로 userSeq가 입력된다.")
            @PathVariable("userSeq") Long userSeq) {

        int followersCnt = userService.getFollowersCount(userSeq);
        return ResponseEntity.status(HttpStatus.OK).body(followersCnt);
    }
}
