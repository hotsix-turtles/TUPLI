package hotsixturtles.tupli.api;


import hotsixturtles.tupli.dto.UserProfileDto;
import hotsixturtles.tupli.dto.response.ErrorResponse;
import hotsixturtles.tupli.dto.simple.SimpleBadgeDto;
import hotsixturtles.tupli.dto.simple.SimpleUserInfoDto;
import hotsixturtles.tupli.entity.Badge;
import hotsixturtles.tupli.entity.Playroom;
import hotsixturtles.tupli.entity.User;
import hotsixturtles.tupli.entity.UserBadge;
import hotsixturtles.tupli.entity.meta.UserInfo;
import hotsixturtles.tupli.repository.UserInfoRepository;
import hotsixturtles.tupli.repository.UserRepository;
import hotsixturtles.tupli.service.BadgeService;
import hotsixturtles.tupli.service.HomeInfoService;
import hotsixturtles.tupli.service.PlayroomService;
import hotsixturtles.tupli.service.UserInfoService;
import hotsixturtles.tupli.service.token.JwtTokenProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Api(tags = "프로필 정보 API")
public class UserInfoApiController {

    private final UserInfoRepository userInfoRepository;
    private final UserRepository userRepository;

    private final JwtTokenProvider jwtTokenProvider;
    private final UserInfoService userInfoService;
    private final BadgeService badgeService;
    private final HomeInfoService homeInfoService;
    private final PlayroomService playroomService;

    private final MessageSource messageSource;

    /**
     * 타인의 정보 찾기
     * @param userSeq
     * @return
     * 반환 코드 : 200, 204, 404
     */
    @GetMapping("/userinfo/{userSeq}")
    @ApiOperation(value = "유저의 프로필 정보 확인", notes = "uid 저장된 정보가 없을 시 204 반환, 성공 시 200[userInfo 값] 반환")
    public ResponseEntity findUserInfo(@ApiParam(value = "path 로 uid 전달받는다.") @PathVariable("userSeq") Long userSeq,
                                       HttpServletRequest request,
                                       @PageableDefault(size = 30, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable ) {

        UserInfo userInfo = userInfoRepository.findOneByUserSeq(userSeq);

        if(userInfo == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }

        String token = request.getHeader("Authorization");
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            User user = userRepository.findByUserSeq(userSeq);

            List<Object> activities = homeInfoService.getActivites(userSeq, pageable);
            List<Playroom> playrooms = playroomService.getWatchingPlayroom(userSeq);
            UserProfileDto result = new UserProfileDto(user, userInfo, playrooms, activities);
            result.setMeCheck(true);
            return ResponseEntity.ok().body(result);
        }
        else{
            Long myUserSeq = jwtTokenProvider.getUserSeq(token);
            User user = userRepository.findByUserSeq(userSeq);

            List<Object> activities = homeInfoService.getActivites(userSeq, pageable);
            List<Playroom> playrooms = playroomService.getWatchingPlayroom(userSeq);
            UserProfileDto result = new UserProfileDto(user, userInfo, playrooms, activities);
            if(myUserSeq == user.getUserSeq()) result.setMeCheck(true);
            return ResponseEntity.ok().body(result);
        }
    }

    /**
     * 유저의 플레이룸 시청시간 갱신
     * @param time 시청 시간
     * @param token
     * @return
     * 반환 코드 : 200, 403, 404
     */
    @PutMapping("/userinfo/watchtime")
    public ResponseEntity userInfoUpdate(@RequestParam("time") Long time,
                                         @RequestHeader(value = "Authorization") String token) {
        // 토큰 유효 확인 및 유저 정보(UseqSeq) 가져오기
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);

        // UserInfo 갱신
        userInfoService.userInfoUpdateTime(userSeq, time);

        List<UserBadge> userBadges = badgeService.getBadgeList(userSeq);

        List<Long> badges = badgeService.getUserBadgeSeq(userBadges);

        // 배지갱신
        badgeService.checkWatchTime(userSeq, badges);

        return ResponseEntity.ok().body(null);
    }

    /**
     * 유저의 boardupload 횟수 갱신
     * @param token
     * @return
     * 반환 코드 : 200, 403, 404
     */
    @PutMapping("/userinfo/board")
    public ResponseEntity userInfoUpdateBoard(@RequestHeader(value = "Authorization") String token) {
        // 토큰 유효 확인 및 유저 정보(UseqSeq) 가져오기
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);

        // UserInfo 갱신
        userInfoService.userInfoUpdateBoard(userSeq);

        List<UserBadge> userBadges = badgeService.getBadgeList(userSeq);

        List<Long> badges = badgeService.getUserBadgeSeq(userBadges);

        // 배지갱신
        List<Badge> badgeResult = badgeService.checkBoardUpload(userSeq, badges);

        if(badgeResult == null || badgeResult.size() == 0) return ResponseEntity.ok().body(null);
        List<SimpleBadgeDto> result = badgeResult.stream().map(b -> new SimpleBadgeDto(b)).collect(Collectors.toList());
        return ResponseEntity.ok().body(result);
    }



}
