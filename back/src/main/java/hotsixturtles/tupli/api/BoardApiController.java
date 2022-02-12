package hotsixturtles.tupli.api;

import hotsixturtles.tupli.dto.request.BoardRequestDto;
import hotsixturtles.tupli.dto.response.BoardResponseDto;
import hotsixturtles.tupli.dto.response.ErrorResponse;
import hotsixturtles.tupli.dto.simple.SimpleBadgeDto;
import hotsixturtles.tupli.entity.Badge;
import hotsixturtles.tupli.entity.Board;
import hotsixturtles.tupli.entity.UserBadge;
import hotsixturtles.tupli.entity.likes.BoardLikes;
import hotsixturtles.tupli.service.BadgeService;
import hotsixturtles.tupli.service.BoardService;
import hotsixturtles.tupli.service.UserInfoService;
import hotsixturtles.tupli.service.token.JwtTokenProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "게시글 관련 API")
public class BoardApiController {

    private final BoardService boardService;

    private final JwtTokenProvider jwtTokenProvider;

    private final MessageSource messageSource;

    private final BadgeService badgeService;

    private final UserInfoService userInfoService;


    /**
     * 전체 게시글 list 가져오기
     * @return List<BoardDto>
     * 반환 코드 : 200, 204, 404
     */
    @GetMapping("/board/list")
    public ResponseEntity<List<BoardResponseDto>> getBoardList(){
        List<Board> boardList = boardService.getBoardList();
        if (boardList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }

        List<BoardResponseDto> result = boardList.stream().map(b -> new BoardResponseDto(b)).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    /**
     * 게시글 정보 가져오기
     * @param boardId
     * @return board
     * 반환 코드 : 200, 404
     */
    @GetMapping("/board/{boardId}")
    public ResponseEntity<?> getBoard(@PathVariable("boardId") Long boardId){

        Board board = boardService.getBoard(boardId);
        return ResponseEntity.status(HttpStatus.OK).body(new BoardResponseDto(board));
    }

    /**
     * 게시글 추가하기
     * @param token
     * @param board : {title, content}
     * @return null
     * 반환 코드 : 201, 403, 404
     */
    @PostMapping("/board")
    public ResponseEntity<?> addBoard(@RequestHeader(value = "Authorization") String token,
                                      @RequestBody BoardRequestDto board){

        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }

        Long userSeq = jwtTokenProvider.getUserSeq(token);

        Board boardResult = boardService.addBoard(userSeq, board);

        userInfoService.userInfoUpdateBoard(userSeq);

        List<UserBadge> userBadges = badgeService.getBadgeList(userSeq);

        List<Long> badges = badgeService.getUserBadgeSeq(userBadges);

        // 배지갱신
        List<Badge> badgeResult = badgeService.checkBoardUpload(userSeq, badges);

        if(badgeResult == null || badgeResult.size() == 0) return ResponseEntity.ok().body(new BoardResponseDto(boardResult, null));
        List<SimpleBadgeDto> result = badgeResult.stream().map(b -> new SimpleBadgeDto(b)).collect(Collectors.toList());
        
        // 뱃지 확인
        return ResponseEntity.status(HttpStatus.CREATED).body(new BoardResponseDto(boardResult, result));
    }

    /**
     * 게시글 갱신하기
     * @param token
     * @param boardId
     * @param board
     * @return null
     * 반환 코드 : 200, 404
     */
    @PutMapping("/board/{boardId}")
    public ResponseEntity<?> updateBoard(@RequestHeader(value = "Authorization") String token,
                                         @PathVariable("boardId") Long boardId,
                                         @RequestBody Board board){

        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }

        Board boardSaved = boardService.updateBoard(boardId, board);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    /**
     * 게시글 지우기
     * @param token
     * @param boardId
     * @return null
     * 반환 코드 : 200, 403, 404
     */
    @DeleteMapping("/board/{boardId}")
    public ResponseEntity<?> deleteBoard(@RequestHeader(value = "Authorization") String token, @PathVariable("boardId") Long boardId){

        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }

        boardService.deleteBoard(boardId);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    /**
     * 게시글에 좋아요를 눌렀는지 여부 반환
     * @param token
     * @param boardId
     * @return
     */
    @GetMapping("/board/{boardId}/like")
    @ApiOperation(value = "게시글에 좋아요 정보 조회", notes = "유저 정보가 일치하지 않으면 404, '유효하지 않은 토큰입니다' 반환," +
            "좋아요가 된 상태라면 200, 'ok' 반환, 좋아요가 아닌 상태라면 200, null 반환")
    public ResponseEntity<?> getBoardLike(@ApiParam(value = "auth token")
                                          @RequestHeader(value = "Authorization") String token,
                                          @ApiParam(value = "게시글 id") @PathVariable("boardId") Long boardId) {
        // 좋아요를 누른 사람 , 어떤 게시글에 좋아요를 눌렀는지 저장 , 현재 그 게시글의 총 좋아요 수를 return
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage
                            ("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);

        BoardLikes boardLikes = boardService.getBoardLike(userSeq, boardId);
        if (boardLikes == null) {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body("ok");
    }

    /**
     * 게시글에 좋아요 추가하기
     * @param token
     * @param boardId
     * @return
     */
    @PostMapping("/board/{boardId}/like")
    @ApiOperation(value = "게시글에 좋아요 등록", notes = "유저 정보가 일치하지 않으면 404, '유효하지 않은 토큰입니다' 반환," +
            "정상 등록 시 200, null 반환")
    public ResponseEntity<?> addBoardLike(@ApiParam(value = "auth token")
                                          @RequestHeader(value = "Authorization") String token,
                                          @ApiParam(value = "게시글 id") @PathVariable("boardId") Long boardId) {
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.
                            getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);
        // 좋아요를 누른 사람 , 어떤 게시글에 좋아요를 눌렀는지 저장 , 현재 그 게시글의 총 좋아요 수를 return
        boardService.addBoardLike(userSeq, boardId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    /**
     * 게시글에 좋아요 취소하기
     * @param token
     * @param boardId
     * @return
     */
    @DeleteMapping("/board/{boardId}/like")
    @ApiOperation(value = "게시글 좋아요 해제", notes = "유저 정보가 일치하지 않으면 404, '유효하지 않은 토큰입니다' 반환," +
            "정상 등록 시 200, null 반환")
    public ResponseEntity<?> deleteBoardLike(@ApiParam(value = "auth token")
                                             @RequestHeader(value = "Authorization") String token,
                                             @ApiParam(value = "게시글 id") @PathVariable("boardId") Long boardId) {
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.
                            getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);
        // 좋아요를 누른 사람 , 어떤 게시글에 좋아요를 눌렀는지 저장 , 현재 그 게시글의 총 좋아요 수를 return
        boardService.deleteBoardLike(userSeq, boardId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    /**
     * 사용자가 좋아요한 게시글
     * @param token
     * @return
     * * 반환코드 : 200, 403, 404
     */
    @GetMapping("/board/likes")
    public ResponseEntity<?> getPlayroomLiked(@RequestHeader(value = "Authorization") String token)
    {
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);

        List<Board> boards = boardService.getLikedBoards(userSeq);

        List<BoardResponseDto> result = boards.stream().map(b -> new BoardResponseDto(b)).collect(Collectors.toList());

        return ResponseEntity.ok().body(result);
    }

}
