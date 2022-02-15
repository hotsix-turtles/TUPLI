package hotsixturtles.tupli.api;

import hotsixturtles.tupli.dto.PlaylistDto;
import hotsixturtles.tupli.dto.PlayroomDto;
import hotsixturtles.tupli.dto.PlayroomLikesDto;
import hotsixturtles.tupli.dto.request.RequestPlayroomDto;
import hotsixturtles.tupli.dto.response.ErrorResponse;
import hotsixturtles.tupli.dto.response.ResponsePlayroomDto;
import hotsixturtles.tupli.dto.simple.SimpleBadgeDto;
import hotsixturtles.tupli.dto.simple.SimplePlayroomCategoryDto;
import hotsixturtles.tupli.entity.*;
import hotsixturtles.tupli.entity.likes.PlayroomLikes;
import hotsixturtles.tupli.entity.youtube.YoutubeVideo;
import hotsixturtles.tupli.service.*;
import hotsixturtles.tupli.service.list.CategoryListWord;
import hotsixturtles.tupli.service.token.JwtTokenProvider;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PlayroomApiController {

    private final PlayroomService playroomService;

    private final JwtTokenProvider jwtTokenProvider;

    private final MessageSource messageSource;

    private final SearchService searchService;

    private final BadgeService badgeService;

    private final UserInfoService userInfoService;

    private final PlaylistService playlistService;

    private final UserService userService;

    /**
     * 내가 작성한 플레이룸들
     * @param token
     * @param pageable
     * @return
     */
    @GetMapping("/playroom/my")
    public ResponseEntity getMyPlayroom(@RequestHeader(value = "Authorization") String token,
                                      @PageableDefault(size = 50, sort ="id",  direction = Sort.Direction.DESC) Pageable pageable){
        // 유저 정보
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);

        List<Playroom> playrooms = playroomService.getMyPlayroom(userSeq, pageable);
        List<PlayroomDto> playroomDtoList = playrooms.stream().map(x -> new PlayroomDto(x)).collect(Collectors.toList());

        for(PlayroomDto nowPlayroom : playroomDtoList) {
            List<PlaylistDto> plList = new ArrayList<>();
            for (Map.Entry<Long, List<Long>> entry : nowPlayroom.getPlaylists().entrySet()){
                Long playlistId = entry.getKey();
                plList.add(new PlaylistDto(playlistService.getPlaylist(playlistId)));
            }
            nowPlayroom.setPlaylistsInfo(plList);
        }

        return ResponseEntity.status(HttpStatus.OK).body(playroomDtoList);
    }

    /**
     * 플레이룸 리스트 출력 (전체)
     * @return
     * 반환 코드 : 200, 404
     */
    @GetMapping("/playroom/list")
    public ResponseEntity<?> getPlayroomList(HttpServletRequest request){

        List<Playroom> playroomList = playroomService.getPlayroomList();

        String token = request.getHeader("Authorization");
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            List<PlayroomDto> playroomDtoList = playroomList.stream().map(b -> new PlayroomDto(b)).collect(Collectors.toList());

            for(PlayroomDto nowPlayroom : playroomDtoList) {
                List<PlaylistDto> plList = new ArrayList<>();
                for (Map.Entry<Long, List<Long>> entry : nowPlayroom.getPlaylists().entrySet()){
                    Long playlistId = entry.getKey();
                    plList.add(new PlaylistDto(playlistService.getPlaylist(playlistId)));
                }
                nowPlayroom.setPlaylistsInfo(plList);
            }

            return ResponseEntity.status(HttpStatus.OK).body(playroomDtoList);
        } else {
            Long userSeq = jwtTokenProvider.getUserSeq(token);

            User user = userService.getUserByUserseq(userSeq);
            List<PlayroomDto> playroomDtoList = playroomList.stream().map(b -> new PlayroomDto(b, user)).collect(Collectors.toList());

            for(PlayroomDto nowPlayroom : playroomDtoList) {
                List<PlaylistDto> plList = new ArrayList<>();
                for (Map.Entry<Long, List<Long>> entry : nowPlayroom.getPlaylists().entrySet()){
                    Long playlistId = entry.getKey();
                    plList.add(new PlaylistDto(playlistService.getPlaylist(playlistId)));
                }
                nowPlayroom.setPlaylistsInfo(plList);
            }

            return ResponseEntity.status(HttpStatus.OK).body(playroomDtoList);
        }
    }

    /**
     * 입장 전, 플레이룸 정보 가져오기
     * @param playroomId
     * @return
     * 반환 코드: 200, 404
     */
    @GetMapping("/playroom/{playroomId}")
    public ResponseEntity<?> getPlayroom(@PathVariable("playroomId") Long playroomId,
                                         HttpServletRequest request){

        // 회원, 비회원(유효하지 않은 토큰) 구분
        String token = request.getHeader("Authorization");
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            Playroom playroom = playroomService.getPlayroom(playroomId);
            if (playroom == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.status(HttpStatus.OK).body(new PlayroomDto(playroom));
        } else {
            Long userSeq = jwtTokenProvider.getUserSeq(token);
            User user = userService.getUserByUserseq(userSeq);
            Playroom playroom = playroomService.getPlayroom(playroomId);
            if (playroom == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            PlayroomDto result = new PlayroomDto(playroom, user);  // 혹시 모르니 미리 DTO 짜놓고
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
    }

    /**
     * 입장 완료시 도는 로직, 게스트 추가
     * @param playroomId
     * @param token
     * @return
     */
    @PostMapping("/playroom/in/{playroomId}")
    public ResponseEntity<?> addGuest(@PathVariable("playroomId") Long playroomId,
                                      @RequestHeader(value = "Authorization") String token){
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);


        Playroom playroom = playroomService.getPlayroom(playroomId);
        if (!playroom.getGuests().contains(userSeq)) {
            playroomService.addGuest(userSeq, playroom);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }


    /**
     * 플레이룸 방장 변경시 정보 가져오기
     * @param playroomId
     * @return
     * 반환 코드: 200, 404
     */
    @GetMapping("/playroom2/{playroomId}")
    public ResponseEntity<?> getPlayroom2(@PathVariable("playroomId") Long playroomId,
                                         HttpServletRequest request){

        // 회원, 비회원(유효하지 않은 토큰) 구분
        String token = request.getHeader("Authorization");
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            Playroom playroom = playroomService.getPlayroom(playroomId);
            if (playroom == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.status(HttpStatus.OK).body(new PlayroomDto(playroom));
        } else {
            Long userSeq = jwtTokenProvider.getUserSeq(token);
            User user = userService.getUserByUserseq(userSeq);
            Playroom playroom = playroomService.getPlayroom(playroomId);
            if (playroom == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            PlayroomDto result = new PlayroomDto(playroom, user);  // 혹시 모르니 미리 DTO 짜놓고
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
    }

    /**
     * 플레이룸 추가
     * @param token
     * @param playroomDto : {roomTitle, roomContent, roomPublic,roominviteIds,
     *                        roomPlaylists, roomTags, roomVideos, startTime, roomEndTime}
     * @return
     * 반환 코드 : 201, 403, 404
     */
    @PostMapping("/playroom")
    public ResponseEntity<?> addPlayroom(@RequestHeader(value = "Authorization") String token,
                                         @RequestBody RequestPlayroomDto playroomDto){
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);

        PlayroomDto playroomResult = playroomService.addPlayroom(playroomDto, userSeq);

        userInfoService.updatePlayroomMake(userSeq);
        List<UserBadge> userbadges = badgeService.getBadgeList(userSeq);
        List<Long> badges = badgeService.getUserBadgeSeq(userbadges);
        List<Badge> badgeResult = new ArrayList<>();

        badgeResult.addAll(badgeService.checkPlayroomMake(userSeq, badges));

        if(badgeResult.size() == 0) {
            badgeResult = null;
            return ResponseEntity.status(HttpStatus.CREATED).body(playroomResult);
        }

        List<SimpleBadgeDto> bResult = badgeResult.stream().map(b -> new SimpleBadgeDto(b)).collect(Collectors.toList());

        playroomResult.setBadges(bResult);

        return ResponseEntity.status(HttpStatus.CREATED).body(playroomResult);
    }

    /**
     * 플레이룸 방장 변경
     * @param token
     * @param playroomId
     * @return null
     * 반환 코드 : 200, 404
     */
    @PutMapping("/playroom/{playroomId}/user")
    public ResponseEntity changePlayroomUser(@RequestHeader(value = "Authorization") String token,
                                             @PathVariable("playroomId") Long playroomId) {

        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);

        playroomService.changePlayroomUser(userSeq, playroomId);

        return ResponseEntity.ok().body(null);
    }

    /**
     * 플레이룸 단일 변경
     * @param token
     * @param playroomId
     * @param playroomDto  : {roomTitle, roomContent} // 다른 값도 추가 가능
     * @return
     * 반환 코드 : 200, 401, 403, 404
     */
    @PutMapping("/playroom/{playroomId}")
    public ResponseEntity<?> updatePlayroom(@RequestHeader(value = "Authorization") String token,
                                            @PathVariable("playroomId") Long playroomId,
                                            @RequestBody RequestPlayroomDto playroomDto){

        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }

        Long userSeq = jwtTokenProvider.getUserSeq(token);

        PlayroomDto result = playroomService.updatePlayroom(playroomId, playroomDto, userSeq);

        if(result == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    /**
     * 플레이룸 삭제
     * @param token
     * @param playroomId
     * @return
     * 반환코드 : 200, 401, 403, 404
     */
    @DeleteMapping("/playroom/{playroomId}")
    public ResponseEntity<?> deletePlayroom(@RequestHeader(value = "Authorization") String token,
                                            @PathVariable("playroomId") Long playroomId){

        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }

        Long userSeq = jwtTokenProvider.getUserSeq(token);

        Playroom result = playroomService.deletePlayroom(playroomId, userSeq);

        if(result == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    /**
     * 사용자가 좋아요한 플레이룸
     * @param token
     * @return
     * 반환코드 : 200, 403, 404
     */
    @GetMapping("/playroom/likes")
    public ResponseEntity<?> getPlayroomLiked(@RequestHeader(value = "Authorization") String token)
    {
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);

        User user = userService.getUserByUserseq(userSeq);

        List<Playroom> playrooms = playroomService.getLikedPlayrooms(userSeq);

        List<PlayroomDto> playroomDtoList = playrooms.stream().map(b -> new PlayroomDto(b,user)).collect(Collectors.toList());

        for(PlayroomDto nowPlayroom : playroomDtoList) {
            List<PlaylistDto> plList = new ArrayList<>();
            for (Map.Entry<Long, List<Long>> entry : nowPlayroom.getPlaylists().entrySet()){
                Long playlistId = entry.getKey();
                plList.add(new PlaylistDto(playlistService.getPlaylist(playlistId)));
            }
            nowPlayroom.setPlaylistsInfo(plList);
        }

        return ResponseEntity.ok().body(playroomDtoList);
    }

    /**
     * 플레이룸 좋아요 확인
     * @param token
     * @param playroomId
     * @return PlayroomLikesDto
     * 반환코드 : 200, 403, 404
     */
    @GetMapping("/playroom/{playroomId}/like")
    @ApiOperation(value = "플레이룸 좋아요 확인", notes = "유저 정보가 일치하지 않으면 404, '유효하지 않은 토큰입니다' 반환," +
            "정상 등록 시 200, null 반환")
    public ResponseEntity<?> getBoardLike(@ApiParam(value = "auth token")
                                          @RequestHeader(value = "Authorization") String token,
                                          @ApiParam(value = "플레이룸 id") @PathVariable("playroomId") Long playroomId) {
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.
                            getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);
        PlayroomLikes playroomLikes = playroomService.getPlayroomLike(userSeq, playroomId);
        if(playroomLikes == null) return ResponseEntity.status(HttpStatus.OK).body(null);
        return ResponseEntity.status(HttpStatus.OK).body(new PlayroomLikesDto(playroomLikes));
    }

    /**
     * 플레이룸에 좋아요 등록
     * @param token
     * @param playroomId
     * @return
     * 반환 코드 : 200, 403, 404
     */
    @PostMapping("/playroom/{playroomId}/like")
    @ApiOperation(value = "플레이룸에 좋아요 등록", notes = "유저 정보가 일치하지 않으면 404, '유효하지 않은 토큰입니다' 반환," +
            "정상 등록 시 200, null 반환")
    public ResponseEntity<?> addBoardLike(@ApiParam(value = "auth token")
                                          @RequestHeader(value = "Authorization") String token,
                                          @ApiParam(value = "플레이룸 id") @PathVariable("playroomId") Long playroomId) {
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.
                            getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);
        playroomService.addPlayroomLike(userSeq, playroomId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    /**
     * 게시글에 좋아요 취소하기
     * @param token
     * @param playroomId
     * @return
     */
    @DeleteMapping("/playroom/{playroomId}/like")
    @ApiOperation(value = "플레이룸 좋아요 해제", notes = "유저 정보가 일치하지 않으면 404, '유효하지 않은 토큰입니다' 반환," +
            "정상 등록 시 200, null 반환")
    public ResponseEntity<?> deleteBoardLike(@ApiParam(value = "auth token")
                                             @RequestHeader(value = "Authorization") String token,
                                             @ApiParam(value = "플레이룸 id") @PathVariable("playroomId") Long playroomId) {
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.
                            getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);
        playroomService.deletePlayroomLike(userSeq, playroomId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }


    // user count로 order by default가 나을듯 << 현재 count null이라 id로 해놓음
    /**
     * category별 플레이룸 검색
     * @param categoryKeyword
     * @param pageable
     * @param request
     * @return
     */
    @GetMapping("/playroom/category/{categoryKeyword}")
    public ResponseEntity categoryPlayroom(@PathVariable("categoryKeyword") String categoryKeyword,
                                           @PageableDefault(size = 50, sort ="id",  direction = Sort.Direction.DESC) Pageable pageable,
                                           HttpServletRequest request) {
        // 카테고리 분류
        String category = CategoryListWord.CATEGORY_LIST_WORD.getOrDefault(categoryKeyword, "일상");

        // 회원, 비회원(유효하지 않은 토큰) 구분
        String token = request.getHeader("Authorization");
        List<YoutubeVideo> videos = new ArrayList<>();
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            List<Playroom> playrooms = searchService.categoryPlayroom(category, pageable);
            List<SimplePlayroomCategoryDto> response = playrooms.stream().map(x -> new SimplePlayroomCategoryDto(x)).collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            Long userSeq = jwtTokenProvider.getUserSeq(token);
            List<Playroom> playrooms = searchService.categoryPlayroom(category, pageable);
            List<SimplePlayroomCategoryDto> response = new ArrayList<>();
            for (Playroom playroom : playrooms) {
                Boolean isLiked = false;
                if (playroomService.getPlayroomLike(userSeq, playroom.getId()) != null) {
                    isLiked = true;
                }
                SimplePlayroomCategoryDto res = new SimplePlayroomCategoryDto(playroom, isLiked);
                response.add(res);
            }
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

    }

    /**
     * 플레이룸 나감!
     * 회원일 경우, 나갈때 배지 갱신 및 참여자 목록에서 본인 삭제
     * @param playroomId
     * @param watchTime
     * @param request
     * @return
     */
    @PutMapping("/playroom/out/{playroomId}")
    public ResponseEntity playroomOut(@PathVariable("playroomId") Long playroomId,
                                      @RequestBody RoomOutDto watchTime,
                                      HttpServletRequest request) {
        // 회원, 비회원(유효하지 않은 토큰) 구분
        String token = request.getHeader("Authorization");
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            // 비회원
            return ResponseEntity.ok().body(null);
        } else {
            Playroom playroom = playroomService.getPlayroom(playroomId);
            Long userSeq = jwtTokenProvider.getUserSeq(token);

            // 참여자 삭제
            playroomService.deleteGuest(userSeq, playroomId);

            userInfoService.userInfoUpdateTime(userSeq, watchTime.getWatchTime());

            List<UserBadge> userbadges = badgeService.getBadgeList(userSeq);
            List<Long> badges = badgeService.getUserBadgeSeq(userbadges);
            List<Badge> badgeResult = new ArrayList<>();

            badgeResult.addAll(badgeService.checkWatchTime(userSeq, badges));

            ConcurrentHashMap<Integer, Integer> videosCategory = playroom.getPlayroomInfo();

            badgeResult.addAll(badgeService.checkPlayroomWatchGenre(userSeq, badges, watchTime.getWatchTime(), videosCategory));

            if(badgeResult == null || badgeResult.size() == 0) return ResponseEntity.ok().body(null);

            List<SimpleBadgeDto> result = badgeResult.stream().map(x -> new SimpleBadgeDto(x)).collect(Collectors.toList());

            return ResponseEntity.ok().body(result);
        }
    }

    @Data
    static class RoomOutDto {
        private Long watchTime;
    }

}
