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
@Api(tags = "?????? ?????? API")
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
     * ?????? ??????
     * @param request
     * @param bindingResult
     * @return
     * ?????? ?????? : 201/ 404
     */
    @PostMapping("/account/signup")
    @ApiOperation(value = "????????????", notes = "")
    public ResponseEntity signup(@ApiParam(value = "email, username, nickname, password??? ????????????.") @Validated @RequestBody CreateUserRequest request,
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
//        @Length(min=3, max=128, message = "???????????? ?????? ?????????")
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
     * ?????? ??????
     * @param token
     * @return
     */
    @DeleteMapping("/account/withdraw")
    @ApiOperation(value = "????????????", notes = "")
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
     * ?????? ????????? ??????(?????? ????????????) ?????? 5??? ?????????????????? ????????????
     * @param userSeq
     * @return
     */
    @GetMapping("/profile/taste/{userSeq}")
    @ApiOperation(value = "???????????? ?????? ?????? ?????? ?????? 5?????? ??????", notes = "")
    public ResponseEntity profileTaste(@PathVariable("userSeq") Long userSeq) {
        // ?????? ????????? ?????? ????????????
        List<String> taste = userService.getProfileTaste(userSeq);
        return ResponseEntity.ok().body(taste);
    }

    /**
     * ?????? ????????? ???????????? ????????? ????????????
     * @param userSeq
     * @return
     */
    @GetMapping("/profile/tasteInfo/{userSeq}")
    @ApiOperation(value = "???????????? ?????? ?????? ?????? ????????? ?????? ", notes = "")
    public ResponseEntity profileTasteInfo(@PathVariable("userSeq") Long userSeq) {
        // ?????? ????????? ?????? ????????????
        ConcurrentHashMap<String, Integer> tasteInfo = userService.getProfileTasteInfo(userSeq);
        return ResponseEntity.ok().body(tasteInfo);
    }

    /**
     * ???????????? ??????
     * @param token
     * @param request
     * @param bindingResult
     * @return
     */
    @PutMapping("/account/password")
    @ApiOperation(value = "???????????? ??????", notes = "")
    public ResponseEntity passwordChange(@RequestHeader(value = "Authorization") String token,
                                         @Validated @RequestBody passwordChangeRequest request,
                                         BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(bindingResult.getAllErrors().get(0).getDefaultMessage()));
        }

        // ?????? ?????? ??? ??????
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        User user = jwtTokenProvider.getUser(token);

        // ?????? ???????????? ??????
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(messageSource.getMessage("error.wrong.password", null, LocaleContextHolder.getLocale())));
        }

        // ???????????? ??????
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
     * ???????????? ??????(OUATH)
     * @param token
     * @return
     */
    @PutMapping("/account/password/oauth")
    @ApiOperation(value = "??????????????? ???????????? ???????????? ??????", notes = "")
    public ResponseEntity passwordOauthChange(@RequestHeader(value = "Authorization") String token,
                                              @RequestBody passwordOauthChangeRequest request,
                                              BindingResult bindingResult) {


        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(bindingResult.getAllErrors().get(0).getDefaultMessage()));
        }

        // ?????? ?????? ??? ??????
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        User user = jwtTokenProvider.getUser(token);

        // Oauth?????? ??????
        if (!"NO_PASS".equals(user.getPassword())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(messageSource.getMessage("error.wrong.oauthpassword", null, LocaleContextHolder.getLocale())));
        }

        // ???????????? ??????
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
     * ????????? JWT ??????
     * @param userInfo {email, password}
     * @return
     */
    @PostMapping("/account/login")
    @ApiOperation(value = "?????? ?????????", notes = "")
    public ResponseEntity<?> login(@ApiParam(value = "email, password??? ????????????.") @RequestBody Map<String, String> userInfo) {
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
     * ????????? ?????? ??? ???????????? ?????????.
     * @param token
     * @return
     */
    @GetMapping("/account/userInfo")
    @ApiOperation(value = "????????? ??????????????? ??????", notes = "")
    public ResponseEntity getMyProfile(@RequestHeader(value = "Authorization") String token) {
        try {
            User user = jwtTokenProvider.getUser(token);
            return ResponseEntity.ok().body(new UserProfileDto(user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * ????????? ?????? ????????????
     * @param token
     * @return
     */
    @GetMapping("/account/setting")
    @ApiOperation(value = "???????????? ?????? ??????", notes = "")
    public ResponseEntity getSetting(@RequestHeader(value = "Authorization") String token) {
        // ?????? ??? ??????
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
     * ????????? ?????? ??????
     * @param token
     * @param userSettingDto
     * @return
     */
    @PutMapping("/account/setting")
    @ApiOperation(value = "???????????? ?????? ??????", notes = "")
    public ResponseEntity changeSetting(@RequestHeader(value = "Authorization") String token,
                                        @RequestBody UserSettingRequestDto userSettingDto) {
        // ?????? ??? ??????
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
     * token ?????? ??????, ?????? ???????????? ?????? ??????
     * @param request (token)
     * @return
     */
    @GetMapping("/account/tokenvalidate")
    @ApiOperation(value = "???????????? jwt token??? ???????????? ??????", notes = "")
    public ResponseEntity<?> checkTokenValidate(HttpServletRequest request){

        // ?????? ????????? ?????????
        String token = request.getHeader("Authorization");

        if (token == null || !jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.
                            getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }

        return ResponseEntity.ok().body(null);
    }

    /**
     * ?????????????????? ??????
     * @param email
     * @return
     */
    @PutMapping("/account/passwordFind/{email}/nickname/{nickname}")
    @ApiOperation(value = "???????????? ?????? ???, ?????? ??????????????? ????????? ??????", notes = "")
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

        // ?????? ????????????
        mailSendService.sendTmpPassword(user, email);
        return ResponseEntity.ok().body(null);
    }


    /**
     * ?????? ????????? ?????? ????????? ???????????? ??????
     * @param userSeq
     * @return 
     */
    @GetMapping("/account/profile/{userSeq}")
    @ApiOperation(value = "????????? ????????? ??????", notes = "")
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
     * ????????? ?????? ??????
     * @param file
     * @param nickname
     * @param request
     * @return SimpleUpdateProfileDto {content : introduction, nickname, image }
     * @throws IOException
     * ?????? ?????? : 200, 404
     */
    @PutMapping("/profile")
    @ApiOperation(value = "????????? ????????? ?????? ??????", notes = "")
    public ResponseEntity<?> updateProfile(@RequestPart(value = "image", required = false) MultipartFile file,
                                           @RequestPart(value = "introduction", required = false) String introduction,
                                            @RequestPart(value = "nickname", required = false) String nickname,
                                           HttpServletRequest request) throws IOException {

        //utf-8 ?????? ???????????? DB??? ??????
        introduction = URLDecoder.decode(introduction, "UTF-8");
        nickname = URLDecoder.decode(nickname, "UTF-8");
        
        // jwt ?????? ?????? + ?????? ??????
        String token = request.getHeader("Authorization");
        if(!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource
                            .getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);

        // ????????? ????????? ??????
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
     * ????????? ??????
     * ??? ????????? ????????? ????????? ????????? ?????????. ??? ??????id??? ??? ??????id??? ??????????????? ?????????. ??????????????? ????????????
     */
    @PostMapping("/account/follow/{userSeq}")
    @ApiOperation(value = "?????? ???????????????", notes = "")
    public ResponseEntity<?> followUser(
            @ApiParam(value = "Authroization ?????? ????????????.")
            @RequestHeader(value = "Authorization") String token,
            @ApiParam(value = "????????? ??? ????????? userSeq.")
            @PathVariable("userSeq") Long otherUserSeq) {
        // ??? ????????? ????????? ????????? ??????, ????????? ?????????????????? ???????????? ?????? ??????
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.
                            getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        // ??? ?????????????????? id ?????????.
        Long userSeq = jwtTokenProvider.getUserSeq(token);
        UserLikes userLikes = userService.getFollow(userSeq, otherUserSeq);
        // ????????? ?????????????????? ????????? ??????
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
        // ?????? ????????? ????????????
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    /**
     * ???????????? ??????
     */
    @DeleteMapping("/account/follow/{userSeq}")
    @ApiOperation(value = "?????? ???????????? ??????", notes = "")
    public ResponseEntity<?> unfollowUser(
            @ApiParam(value = "Authroization ?????? ????????????.")
            @RequestHeader(value = "Authorization") String token,
            @ApiParam(value = "???????????? ??? ????????? userSeq.")
            @PathVariable("userSeq") Long otherUserSeq) {
        // ??? ????????? ????????? ????????? ??????, ????????? ?????????????????? ???????????? ?????? ??????
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
        // ?????? ???????????? ????????????
        return ResponseEntity.status(HttpStatus.OK).body("already followed");
    }

    /**
     * ????????? ????????? ????????? ???????????? ??????
     */
    @GetMapping("/account/follow/{userSeq}")
    @ApiOperation(value = "?????? ????????? ??????????????????, ???????????? ????????? ??????", notes = "")
    public ResponseEntity<?> getFollowUser(
            @ApiParam(value = "Authroization ?????? ????????????.")
            @RequestHeader(value = "Authorization") String token,
            @ApiParam(value = "????????? ??? ????????? userSeq.")
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
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);  // ?????????
        }
        return ResponseEntity.status(HttpStatus.OK).body("ok");  // ??????
    }


    /**
     * ?????? ????????? ????????? ??????
     * @return
     */
    @PostMapping("/account/dislike/{userSeq}")
    @ApiOperation(value = "????????? ????????? ??????", notes = "")
    public ResponseEntity<?> dislikeUser(
            @ApiParam(value = "Authroization ?????? ????????????.")
            @RequestHeader(value = "Authorization") String token,
            @ApiParam(value = "????????? ??? ????????? userSeq.")
            @PathVariable("userSeq") Long otherUserSeq) {
        // ??? ????????? ????????? ????????? ??????, ????????? ?????????????????? ???????????? ?????? ??????
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.
                            getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);
        UserDislikes userDislikes = userService.getDislike(userSeq, otherUserSeq);
        // ????????? ?????????????????? ????????? ??????
        if (userDislikes == null) {
            userService.dislike(userSeq, otherUserSeq);
            return ResponseEntity.status(HttpStatus.OK).body("checked dislike");
        }
        // ?????? ????????? ????????????
        return ResponseEntity.status(HttpStatus.OK).body("already checked dislike");

    }

    /**
     * ?????? ????????? ????????? ????????? ????????????
     * @return
     */
    @DeleteMapping("/account/dislike/{userSeq}")
    @ApiOperation(value = "????????? ????????? ????????????", notes = "")
    public ResponseEntity<?> undoDislikeUser(
            @ApiParam(value = "Authroization ?????? ????????????.")
            @RequestHeader(value = "Authorization") String token,
            @ApiParam(value = "???????????? ????????? ????????? userSeq.")
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
        // ?????? ????????? ??????????????????.
        return ResponseEntity.status(HttpStatus.OK).body("already unchecked dislike");
    }

    /**
     * ????????? ????????? ????????? ???????????? ??????
     */
    @GetMapping("/account/dislike/{userSeq}")
    @ApiOperation(value = "?????? ????????? ????????? ????????? ????????? ??????", notes = "")
    public ResponseEntity<?> getDislikeUser(
            @ApiParam(value = "Authroization ?????? ????????????.")
            @RequestHeader(value = "Authorization") String token,
            @ApiParam(value = "????????? ??? ????????? userSeq.")
            @PathVariable("userSeq") Long otherUserSeq) {
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.
                            getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);
        UserDislikes userDislikes = userService.getDislike(userSeq, otherUserSeq);
        if (userDislikes == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);  // ?????????
        }
        return ResponseEntity.status(HttpStatus.OK).body("ok");  // ??????
    }

    /**
     * ?????? ????????? ?????????(??? ????????? ????????? ??? ??????) ??????
     */
    @GetMapping("/profile/followers/{userSeq}")
    @ApiOperation(value = "?????? ????????? ????????? ????????? ??????", notes = "")
    public ResponseEntity<?> getFollowers(
            @ApiParam(value = "auth token")
            @RequestHeader(value = "Authorization") String token,
            @ApiParam(value = "path??? userSeq??? ????????????.")
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
     * ?????? ????????? ?????????(??? ????????? ????????? ??? ??????) ??? ??????
     */
    @GetMapping("/profile/followers/{userSeq}/count")
    @ApiOperation(value = "?????? ????????? ????????? ????????? ??????", notes = "")
    public ResponseEntity<?> getFollowers(
            @ApiParam(value = "path??? userSeq??? ????????????.")
            @PathVariable("userSeq") Long userSeq) {

        int followersCnt = userService.getFollowersCount(userSeq);
        return ResponseEntity.status(HttpStatus.OK).body(followersCnt);
    }

    /**
     * ?????? ????????? ??? ?????? ??????
     * @param token
     * @return
     */
    @GetMapping("/profile/followings/{userSeq}")
    @ApiOperation(value = "???????????? ????????? ?????? ??? ??????", notes = "")
    public ResponseEntity<?> getMyFollowees(
            @ApiParam(value = "auth token")
            @RequestHeader(value = "Authorization") String token,
            @ApiParam(value = "path??? userSeq??? ????????????.")
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
     * ????????? ???????????? ??????
     * @param token
     * @return
     */
    @GetMapping("/profile/cofollowers/{userSeq}")
    @ApiOperation(value = "???????????? ???????????? ???????????? ?????? ????????? ??????", notes = "")
    public ResponseEntity<?> getMyCoFollowers(
            @ApiParam(value = "auth token")
            @RequestHeader(value = "Authorization") String token,
            @ApiParam(value = "path??? userSeq??? ????????????.")
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
