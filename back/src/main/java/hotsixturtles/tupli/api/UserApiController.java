package hotsixturtles.tupli.api;

import hotsixturtles.tupli.dto.UserDto;
import hotsixturtles.tupli.dto.UserProfileDto;
import hotsixturtles.tupli.dto.UserSettingDto;
import hotsixturtles.tupli.dto.request.UserSettingRequestDto;
import hotsixturtles.tupli.dto.response.ErrorResponse;
import hotsixturtles.tupli.dto.simple.SimpleBadgeDto;
import hotsixturtles.tupli.dto.simple.SimpleUpdateProfileDto;
import hotsixturtles.tupli.dto.simple.SimpleUserDto;
import hotsixturtles.tupli.entity.*;
import hotsixturtles.tupli.entity.auth.ProviderType;
import hotsixturtles.tupli.entity.auth.RoleType;
import hotsixturtles.tupli.entity.likes.UserDislikes;
import hotsixturtles.tupli.entity.likes.UserLikes;
import hotsixturtles.tupli.entity.meta.UserInfo;
import hotsixturtles.tupli.repository.UserInfoRepository;
import hotsixturtles.tupli.repository.UserRepository;
import hotsixturtles.tupli.repository.UserSettingRepository;
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
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "유저 관련 API")
public class UserApiController {

    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;

    private final UserService userService;
    private final UserSettingService userSettingService;
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
    @ApiOperation(value = "회원가입", notes = "")
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
//        @Size(min=3, max=128, message = "{error.size.email}")
        @Email(message = "{error.format.email}", regexp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")
        @NotNull(message = "{email.notempty}")
        private String email;
        private String nickname;
        @Size(min=4, max=12, message = "{error.size.password}")
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
    @ApiOperation(value = "회원탈퇴", notes = "")
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
     * 해당 유저의 취향(행동 분석결과) 상위 5개 정리해놓은것 가져가기
     * @param userSeq
     * @return
     */
    @GetMapping("/profile/taste/{userSeq}")
    @ApiOperation(value = "사용자의 취향 분석 결과 상위 5개를 리턴", notes = "")
    public ResponseEntity profileTaste(@PathVariable("userSeq") Long userSeq) {
        // 해당 유저의 취향 가져가기
        List<String> taste = userService.getProfileTaste(userSeq);
        return ResponseEntity.ok().body(taste);
    }

    /**
     * 해당 유저의 취향정보 통째로 가져가기
     * @param userSeq
     * @return
     */
    @GetMapping("/profile/tasteInfo/{userSeq}")
    @ApiOperation(value = "사용자의 취향 분석 결과 전체를 리턴 ", notes = "")
    public ResponseEntity profileTasteInfo(@PathVariable("userSeq") Long userSeq) {
        // 해당 유저의 취향 가져가기
        ConcurrentHashMap<String, Integer> tasteInfo = userService.getProfileTasteInfo(userSeq);
        return ResponseEntity.ok().body(tasteInfo);
    }

    /**
     * 비밀번호 변경
     * @param token
     * @param request
     * @param bindingResult
     * @return
     */
    @PutMapping("/account/password")
    @ApiOperation(value = "비밀번호 변경", notes = "")
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
     * 비밀번호 변경(OUATH)
     * @param token
     * @return
     */
    @PutMapping("/account/password/oauth")
    @ApiOperation(value = "소셜로그인 사용자의 비밀번호 변경", notes = "")
    public ResponseEntity passwordOauthChange(@RequestHeader(value = "Authorization") String token,
                                              @RequestBody passwordOauthChangeRequest request,
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

        // Oauth유저 확인
        if (!"NO_PASS".equals(user.getPassword())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(messageSource.getMessage("error.wrong.oauthpassword", null, LocaleContextHolder.getLocale())));
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
    static class passwordOauthChangeRequest {
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
    @ApiOperation(value = "일반 로그인", notes = "")
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
    @ApiOperation(value = "사용자 프로필정보 리턴", notes = "")
    public ResponseEntity getMyProfile(@RequestHeader(value = "Authorization") String token) {
        try {
            User user = jwtTokenProvider.getUser(token);
            return ResponseEntity.ok().body(new UserProfileDto(user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * 사용자 설정 가져가기
     * @param token
     * @return
     */
    @GetMapping("/account/setting")
    @ApiOperation(value = "사용자의 설정 리턴", notes = "")
    public ResponseEntity getSetting(@RequestHeader(value = "Authorization") String token) {
        // 인증 및 대상
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);

        UserSetting userSetting = userSettingService.getSetting(userSeq);

        return ResponseEntity.ok().body(new UserSettingDto(userSetting));
    }

    /**
     * 사용자 설정 변경
     * @param token
     * @param userSettingDto
     * @return
     */
    @PutMapping("/account/setting")
    @ApiOperation(value = "사용자의 설정 수정", notes = "")
    public ResponseEntity changeSetting(@RequestHeader(value = "Authorization") String token,
                                        @RequestBody UserSettingRequestDto userSettingDto) {
        // 인증 및 대상
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);
        userSettingService.changeSetting(userSeq, userSettingDto);

        return ResponseEntity.ok().body(null);
    }


    /**
     * token 유효 확인, 자동 로그아웃 등에 활용
     * @param request (token)
     * @return
     */
    @GetMapping("/account/tokenvalidate")
    @ApiOperation(value = "사용자의 jwt token이 유효한지 체크", notes = "")
    public ResponseEntity<?> checkTokenValidate(HttpServletRequest request){

        // 인증 확인후 돌리기
        String token = request.getHeader("Authorization");

        if (token == null || !jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.
                            getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }

        return ResponseEntity.ok().body(null);
    }

    /**
     * 임시비밀번호 발송
     * @param email
     * @return
     */
    @PutMapping("/account/passwordFind/{email}/nickname/{nickname}")
    @ApiOperation(value = "비밀번호 변경 시, 임시 비밀번호를 메일로 전송", notes = "")
    public ResponseEntity passwordFind(@PathVariable("email") String email,
                                       @PathVariable("nickname") String nickname) {

        User user = userRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(messageSource.getMessage("error.none.user", null, LocaleContextHolder.getLocale())));
        }
        if (user.getNickname() != nickname) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(messageSource.getMessage("error.wrong.nickname", null, LocaleContextHolder.getLocale())));
        }

        // 임시 비밀번호
        mailSendService.sendTmpPassword(user, email);
        return ResponseEntity.ok().body(null);
    }


    /**
     * 타인 프로필 정보 간단히 긁어오기 함수
     * @param userSeq
     * @return 
     */
    @GetMapping("/account/profile/{userSeq}")
    @ApiOperation(value = "유저의 정보를 리턴", notes = "")
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
    @ApiOperation(value = "사용자 프로필 정보 수정", notes = "")
    public ResponseEntity<?> updateProfile(@RequestPart(value = "image", required = false) MultipartFile file,
                                           @RequestPart(value = "introduction", required = false) String introduction,
                                            @RequestPart(value = "nickname", required = false) String nickname,
                                           HttpServletRequest request) throws IOException {

        //utf-8 내용 적용해서 DB에 넣기
        introduction = URLDecoder.decode(introduction, "UTF-8");
        nickname = URLDecoder.decode(nickname, "UTF-8");
        
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
    @ApiOperation(value = "유저 팔로우하기", notes = "")
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

            badgeResult = badgeService.checkFollowees(userSeq, badges);

            if(badgeResult == null || badgeResult.size() == 0) {
                return ResponseEntity.status(HttpStatus.CREATED).body(null);
            }
            List<SimpleBadgeDto> badgeDtoResult = badgeResult.stream().map(b -> new SimpleBadgeDto(b)).collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.CREATED).body(badgeDtoResult);
        }
        // 이미 팔로우 되어있음
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    /**
     * 언팔로우 하기
     */
    @DeleteMapping("/account/follow/{userSeq}")
    @ApiOperation(value = "유저 언팔로우 하기", notes = "")
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
    @ApiOperation(value = "해당 유저를 팔로우했는지, 안했는지 여부를 리턴", notes = "")
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
        if(userSeq == otherUserSeq){
            return ResponseEntity.status(HttpStatus.OK).body("me");
        }
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
    @ApiOperation(value = "사용자 싫어요 하기", notes = "")
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
    @ApiOperation(value = "사용자 싫어요 해제하기", notes = "")
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
    @ApiOperation(value = "해당 유저를 싫어요 했는지 여부를 리턴", notes = "")
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
    @ApiOperation(value = "해당 유저의 팔로워 목록을 리턴", notes = "")
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

        List<UserLikes> followerList = userService.getFollowers(userSeq);

        List<SimpleUserDto> response = followerList.stream()
                .map(x -> new SimpleUserDto(x.getFromUser())).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 해당 유저의 팔로워(이 유저를 팔로우 한 사람) 수 보기
     */
    @GetMapping("/profile/followers/{userSeq}/count")
    @ApiOperation(value = "해당 유저의 팔로우 목록을 리턴", notes = "")
    public ResponseEntity<?> getFollowers(
            @ApiParam(value = "path로 userSeq가 입력된다.")
            @PathVariable("userSeq") Long userSeq) {

        int followersCnt = userService.getFollowersCount(userSeq);
        return ResponseEntity.status(HttpStatus.OK).body(followersCnt);
    }

    /**
     * 내가 팔로우 한 유저 목록
     * @param token
     * @return
     */
    @GetMapping("/profile/followings/{userSeq}")
    @ApiOperation(value = "사용자의 팔로우 유저 수 리턴", notes = "")
    public ResponseEntity<?> getMyFollowees(
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

        Long tokenUserSeq = jwtTokenProvider.getUserSeq(token);

        List<UserLikes> followerList = userService.getFollowees(tokenUserSeq);

        List<SimpleUserDto> response = followerList.stream()
                .map(x -> new SimpleUserDto(x.getToUser())).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 자신의 맞팔로워 목록
     * @param token
     * @return
     */
    @GetMapping("/profile/cofollowers/{userSeq}")
    @ApiOperation(value = "사용자와 맞팔로우 하고있는 유저 목록을 리턴", notes = "")
    public ResponseEntity<?> getMyCoFollowers(
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

        Long tokenUserSeq = jwtTokenProvider.getUserSeq(token);

        List<UserLikes> followerList = userService.getFollowers(tokenUserSeq);
        List<SimpleUserDto> result = new ArrayList<>();
        List<User> followers = new ArrayList<>();
        for(UserLikes userLikes : followerList){
            followers.add(userLikes.getFromUser());
        }

        List<UserLikes> followeeList = userService.getFollowees(tokenUserSeq);
        for(UserLikes userLikes : followeeList){
            User myFollowee = userLikes.getToUser();
            for(User myFollower : followers){
                if(myFollower.getUserSeq() == myFollowee.getUserSeq() && !result.contains(new SimpleUserDto(myFollower))){
                    result.add(new SimpleUserDto(myFollower));
                    break;
                }
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
