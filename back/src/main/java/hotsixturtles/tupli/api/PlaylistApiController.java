package hotsixturtles.tupli.api;

import hotsixturtles.tupli.dto.PlaylistCommentDto;
import hotsixturtles.tupli.dto.PlaylistDto;
import hotsixturtles.tupli.dto.PlayroomDto;
import hotsixturtles.tupli.dto.params.PlaylistSearchCondition;
import hotsixturtles.tupli.dto.request.PlaylistRequest;
import hotsixturtles.tupli.dto.response.ErrorResponse;
import hotsixturtles.tupli.dto.response.IdResponse;
import hotsixturtles.tupli.dto.simple.SimpleBadgeDto;
import hotsixturtles.tupli.dto.simple.SimplePlaylistCategoryDto;
import hotsixturtles.tupli.dto.simple.SimplePlaylistDto;
import hotsixturtles.tupli.entity.*;
import hotsixturtles.tupli.entity.likes.PlaylistLikes;
import hotsixturtles.tupli.service.*;
import hotsixturtles.tupli.service.list.CategoryListWord;
import hotsixturtles.tupli.service.token.JwtTokenProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "플레이리스트 관련 API")
public class PlaylistApiController {

    private final JwtTokenProvider jwtTokenProvider;
    private final MessageSource messageSource;

    private final PlaylistService playlistService;
    private final FlaskService flaskService;
    private final PlaylistCommentService playlistCommentService;
    private final SearchService searchService;
    private final UserService userService;

    private final UserInfoService userInfoService;
    private final BadgeService badgeService;

    /**
     * 플레이 리스트 추가
     * @param token
     * @param playlistRequest
     * @return 
     * 반환 코드 : 201, 403, 404
     * 고민사항 : ID 또는 PlaylistDto 반환이라도 해줘야 하는지 고민
     */
    @PostMapping("/playlist")
    @ApiOperation(value = "플레이리스트를 만듭니다.", notes = "플레이리스트를 만듭니다.\n인증정보가 틀리면 401에러가 발생합니다.")
    public ResponseEntity addPlaylist(@RequestHeader(value = "Authorization") String token,
                                      @RequestBody PlaylistRequest playlistRequest){
        // 유저 정보
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);

        Playlist playlist = playlistService.addPlaylist(userSeq, playlistRequest);

        // 추천 고도화 (플라스크)
        try {
            flaskService.recommendPlaylistFlask(playlist);
        } catch (Exception e) {
            e.printStackTrace();
        }

        userInfoService.updatePlaylistMake(userSeq);
        List<UserBadge> userbadges = badgeService.getBadgeList(userSeq);
        List<Long> badges = badgeService.getUserBadgeSeq(userbadges);
        List<Badge> badgeResult = new ArrayList<>();

        badgeResult.addAll(badgeService.checkPlaylistMake(userSeq, badges));

        if(badgeResult.size() == 0) return ResponseEntity.status(HttpStatus.CREATED).body(new IdResponse(playlist.getId(), null));

        List<SimpleBadgeDto> badgeDtoResult = badgeResult.stream().map(b -> new SimpleBadgeDto(b)).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.CREATED).body(new IdResponse(playlist.getId(), badgeDtoResult));
    }

    /**
     * 플레이리스트 단일 GET
     * @param id
     * @return
     * 반환 코드 : 200, 404
     */
    @GetMapping("/playlist/{id}")
    @ApiOperation(value = "id에 해당하는 플레이리스트 정보를 리턴합니다.", notes = "id에 해당하는 플레이리스트 정보를 리턴합니다." +
            "\n비회원일 경우 플레이리스트 정보에 유저 정보가 담기지 않습니다.")
    public ResponseEntity getPlaylist(@PathVariable("id") Long id,
                                      HttpServletRequest request){
        // 회원, 비회원(유효하지 않은 토큰) 구분
        String token = request.getHeader("Authorization");
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            Playlist playlist = playlistService.getPlaylist(id);
            List<SimplePlaylistDto> recommendPlaylist = playlistService.getRecommendPlaylist(playlist.getRecommendPlaylists());
            PlaylistDto result = new PlaylistDto(playlist);
            result.setRecommendPlaylists(recommendPlaylist);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            Long userSeq = jwtTokenProvider.getUserSeq(token);
            Playlist playlist = playlistService.getPlaylist(id);
            List<SimplePlaylistDto> recommendPlaylist = playlistService.getRecommendPlaylist(playlist.getRecommendPlaylists());
            User user = userService.getUserByUserseq(userSeq);
            PlaylistDto result = new PlaylistDto(playlist, user);
            result.setRecommendPlaylists(recommendPlaylist);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
    }

    /**
     * 내가 작성한 플레이리스트들
     * @param token
     * @param pageable
     * @return
     */
    @GetMapping("/playlist/my")
    @ApiOperation(value = "자신이 생성한 플레이리스트 목록을 리턴받습니다.", notes = "자신이 생성한 플레이리스트 목록을 리턴받습니다.")
    public ResponseEntity getMyPlaylist(@RequestHeader(value = "Authorization") String token,
                                      @PageableDefault(size = 50, sort ="id",  direction = Sort.Direction.DESC) Pageable pageable){
        // 유저 정보
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);

        List<Playlist> playlists = playlistService.getMyPlaylist(userSeq, pageable);
        List<PlaylistDto> result = playlists.stream().map(x -> new PlaylistDto(x)).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    /**
     * 플레이리스트 리스트 출력 (전체)
     * @return
     * 반환 코드 : 200, 404
     */
    @GetMapping("/playlist/list")
    @ApiOperation(value = "현재 존재하는 모든 플레이리스트 목록을 리턴받습니다.",
            notes = "현재 존재하는 모든 플레이리스트 목록을 리턴받습니다.")
    public ResponseEntity<?> getPlaylistList(HttpServletRequest request){
        // 회원, 비회원(유효하지 않은 토큰) 구분
        String token = request.getHeader("Authorization");
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            List<Playlist> Playlist = playlistService.getPlaylistList();
            List<PlaylistDto> result = Playlist.stream().map(x -> new PlaylistDto(x)).collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            Long userSeq = jwtTokenProvider.getUserSeq(token);
            List<Playlist> Playlist = playlistService.getPlaylistList();
            User user = userService.getUserByUserseq(userSeq);
            List<PlaylistDto> result = Playlist.stream().map(x -> new PlaylistDto(x, user)).collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
    }



    /**
     * 플레이리스트 단일 UPDATE
     * @param token
     * @param id
     * @param playlistRequest
     * @return
     * 반환 코드 : 200, 404
     */
    @PutMapping("/playlist/{id}")
    @ApiOperation(value = "id에 해당하는 플레이리스트를 수정합니다.", notes = "id에 해당하는 플레이리스트를 수정합니다.")
    public ResponseEntity<?> updatePlaylist(@RequestHeader(value = "Authorization") String token,
                                         @PathVariable("id") Long id,
                                            @RequestBody PlaylistRequest playlistRequest){
        // 유저 인증
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }

        playlistService.updatePlaylist(id, playlistRequest);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    /**
     * 플레이리스트 단일 Delete
     * @param token
     * @param id
     * @return
     * 반환 코드 : 204, 404
     */
    @DeleteMapping("/playlist/{id}")
    @ApiOperation(value = "id에 해당하는 플레이리스트를 삭제합니다.", notes = "id에 해당하는 플레이리스트를 삭제합니다.")
    public ResponseEntity<?> deletePlaylist(@RequestHeader(value = "Authorization") String token, @PathVariable("id") Long id){
        // 유저 인증
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }

        playlistService.deletePlaylist(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    /**
     * 플레이리스트 단일 좋아요 여부
     * @param token
     * @param id
     * @return
     * 반환코드 : 200 204 404
     */
    @GetMapping("/playlist/{id}/like")
    @ApiOperation(value = "id에 해당하는 플레이리스트를 좋아요한 회원 목록을 리턴합니다.",
            notes = "id에 해당하는 플레이리스트를 좋아요한 회원 목록을 리턴합니다.")
    public ResponseEntity getPlaylistLike(@RequestHeader(value = "Authorization") String token,
                                          @PathVariable("id") Long id) {
        // 유저 인증 & 정보
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage
                            ("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);

        PlaylistLikes playlistLike = playlistService.getPlaylistLike(userSeq, id);
        if (playlistLike == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);  // 안했음
        }
        return ResponseEntity.status(HttpStatus.OK).body("ok");  // 했음
    }


    /**
     * 플레이리스트 단일 좋아요
     * @param token
     * @param id
     * @return
     */
    @PostMapping("/playlist/{id}/like")
    @ApiOperation(value = "id에 해당하는 플레이리스트를 좋아요 합니다.",
            notes = "id에 해당하는 플레이리스트를 좋아요 합니다.")
    public ResponseEntity<?> addPlaylistLike(@RequestHeader(value = "Authorization") String token,
                                             @PathVariable("id") Long id) {
        // 유저 인증 & 정보
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.
                            getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);

        playlistService.addPlaylistLike(userSeq, id);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }


    /**
     * 플레이리스트 단일 좋아요 취소
     * @param token
     * @param id
     * @return
     */
    @DeleteMapping("/playlist/{id}/like")
    @ApiOperation(value = "id에 해당하는 플레이리스트를 좋아요 해제합니다.",
            notes = "id에 해당하는 플레이리스트를 좋아요 해제합니다.")
    public ResponseEntity<?> deletePlaylistLike(@RequestHeader(value = "Authorization") String token,
                                                @PathVariable("id") Long id) {
        // 유저 인증 & 정보
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.
                            getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);

        playlistService.deletePlaylistLike(userSeq, id);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    /**
     * 플레이리스트 단순 검색
     * @param keyword type(최신순, 관련순,  + 제목 // 검색에 필요한 기준 추가가능 (parameter 추가)
     * @param pageable : 예시 => {sort=roomTitle,desc ...} size, page 값 따로 넘길 수 있음
     * @return
     * 반환 코드 : 200, 404
     */
    @GetMapping("/playlist/search")
    @ApiOperation(value = "검색어에 해당하는 플레이리스트 목록을 리턴합니다.",
            notes = "검색어에 해당하는 플레이리스트 목록을 리턴합니다.\n" +
                    "order 에는 'relevance', 'date'가 올 수 있습니다.")
    public ResponseEntity searchPlaylistSimple(@RequestParam(value = "keyword") String keyword,
                                               @RequestParam(value = "order") String order,
                                               @PageableDefault(size = 1000) Pageable pageable,
                                               HttpServletRequest request) {
        PlaylistSearchCondition playlistSearchCondition = new PlaylistSearchCondition();
        playlistSearchCondition.setKeyword(keyword);
        List<Playlist> playlists = playlistService.searchPlaylistSimple(playlistSearchCondition, order, pageable);
        SearchHistory searchHistory = new SearchHistory(null, "플레이리스트",playlistSearchCondition.getKeyword().trim(),0, 0);
        searchService.addScoreNum(searchHistory);

        String token = request.getHeader("Authorization");
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            List<PlaylistDto> response = playlists.stream().map(x -> new PlaylistDto(x)).collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            Long userSeq = jwtTokenProvider.getUserSeq(token);
            User user = userService.getUserByUserseq(userSeq);
            List<PlaylistDto> response = playlists.stream().map(x -> new PlaylistDto(x, user)).collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }

    /**
     * 카테고리 분류별 반환 (회원, 비회원 구분 있음)
     * @param categoryKeyword
     * @param pageable
     * @param request
     * @return
     */
    @GetMapping("/playlist/category/{categoryKeyword}")
    @ApiOperation(value = "카데고리 키워드에 해당하는 플레이리스트 목록을 반환합니다.", notes = "무야호")
    public ResponseEntity categoryPlaylist(@PathVariable("categoryKeyword") String categoryKeyword,
                                           @PageableDefault(size = 50, sort ="id",  direction = Sort.Direction.DESC) Pageable pageable,
                                           HttpServletRequest request) {
        // 카테고리 분류
        String category = CategoryListWord.CATEGORY_LIST_WORD.getOrDefault(categoryKeyword, "일상");

        // 회원, 비회원(유효하지 않은 토큰) 구분
        String token = request.getHeader("Authorization");
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            List<Playlist> playlists = searchService.categoryPlaylist(category, pageable);
            List<SimplePlaylistCategoryDto> response = playlists.stream().map(x -> new SimplePlaylistCategoryDto(x)).collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            Long userSeq = jwtTokenProvider.getUserSeq(token);
            List<Playlist> playlists = searchService.categoryPlaylist(category, pageable);
            List<SimplePlaylistCategoryDto> response = new ArrayList<>();
            for (Playlist playlist : playlists) {
                Boolean isLiked = false;
                if (playlistService.getPlaylistLike(userSeq, playlist.getId()) != null) {
                    isLiked = true;
                }
                SimplePlaylistCategoryDto res = new SimplePlaylistCategoryDto(playlist, isLiked);
                response.add(res);
            }
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

    }

    /**
     * playlist 댓글 리스트 가져오기
     * @param playlistId
     * @return List<comment>
     * 반환 코드 : 200, 204, 404
     */
    @GetMapping("/playlist/{playlistId}/comment")
    @ApiOperation(value = "플레이리스트의 댓글 목록을 리턴합니다.", notes = "playlistId에 해당하는 플레이리스트 목록을 리턴합니다.")
    public ResponseEntity<List<PlaylistCommentDto>> getCommentList(@PathVariable("playlistId") Long playlistId)
    {

        List<PlaylistComment> commentList = playlistCommentService.getCommentList(playlistId);

        if (commentList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }

        List<PlaylistCommentDto> result = commentList.stream().map(b -> new PlaylistCommentDto(b)).collect(Collectors.toList());

        return ResponseEntity.ok().body(result);
    }

    /**
     * playlist 댓글 등록
     * @param token
     * @param playlistId
     * @param playlistComment : {content}
     * @return null
     *반환 코드 : 201, 403, 404
     */
    @PostMapping("/playlist/{playlistId}/comment")
    @ApiOperation(value = "플레이리스트의 댓글을 작성합니다.", notes = "playlistId에 해당하는 플레이리스트 댓글을 작성합니다.")
    public ResponseEntity<?> addComment(@RequestHeader(value = "Authorization") String token,
                                        @PathVariable("playlistId") Long playlistId,
                                        @RequestBody PlaylistComment playlistComment){
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }

        Long userSeq = jwtTokenProvider.getUserSeq(token);

        PlaylistComment commentResult = playlistCommentService.addComment(userSeq, playlistId, playlistComment);

        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    /**
     * playlist 댓글 수정
     * @param token
     * @param commentId
     * @param playlistComment : {content}
     * @return null
     * 반환 코드 : 200, 401, 403, 404
     */
    @PutMapping("/playlist/{commentId}/comment")
    @ApiOperation(value = "플레이리스트의 댓글을 수정합니다.", notes = "playlistId에 해당하는 플레이리스트 댓글을 수정합니다.")
    public ResponseEntity<?> updateComment(@RequestHeader(value = "Authorization") String token,
                                           @PathVariable("commentId") Long commentId,
                                           @RequestBody PlaylistComment playlistComment){

        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }

        Long userSeq = jwtTokenProvider.getUserSeq(token);

        PlaylistComment commentSaved = playlistCommentService.updateComment(userSeq, commentId, playlistComment);

        if(commentSaved == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    /**
     * playlist 댓글 삭제
     * @param token
     * @param commentId
     * @return null
     * 반환 코드 : 200, 401, 403, 404
     */
    @DeleteMapping("/playlist/{commentId}/comment")
    @ApiOperation(value = "플레이리스트의 댓글을 삭제합니다.", notes = "playlistId에 해당하는 플레이리스트 댓글을 삭제합니다.")
    public ResponseEntity<?> deleteComment(@RequestHeader(value = "Authorization") String token,
                                           @PathVariable("commentId") Long commentId){

        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);

        Long result = playlistCommentService.deleteComment(commentId, userSeq);

        if(result == -1L){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }


    /**
     * 사용자가 좋아요 누른 플레이리스트
     * @param token
     * @return
     * 반환 코드 : 200, 403, 404
     */
    @GetMapping("/playlist/likes")
    @ApiOperation(value = "사용자가 좋아요 누른 플레이리스트의 목록을 리턴합니다.", notes = "사용자가 좋아요 누른 플레이리스트의 목록을 리턴합니다.")
    public ResponseEntity<?> getPlaylistLiked(@RequestHeader(value = "Authorization") String token)
    {
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);
        User user = userService.getUserByUserseq(userSeq);
        List<Playlist> playlists = playlistService.getLikedPlaylists(userSeq);

        List<PlaylistDto> result = playlists.stream().map(b -> new PlaylistDto(b, user)).collect(Collectors.toList());

        return ResponseEntity.ok().body(result);
    }

}
