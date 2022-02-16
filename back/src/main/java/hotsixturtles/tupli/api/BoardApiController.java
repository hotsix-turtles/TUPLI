package hotsixturtles.tupli.api;

import hotsixturtles.tupli.dto.CommentDto;
import hotsixturtles.tupli.dto.PlayroomDto;
import hotsixturtles.tupli.dto.request.BoardRequestDto;
import hotsixturtles.tupli.dto.response.BoardResponseDto;
import hotsixturtles.tupli.dto.response.ErrorResponse;
import hotsixturtles.tupli.dto.simple.SimpleBadgeDto;
import hotsixturtles.tupli.dto.simple.SimpleCommentDto;
import hotsixturtles.tupli.entity.*;
import hotsixturtles.tupli.entity.likes.BoardLikes;
import hotsixturtles.tupli.service.*;
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

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
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

    private final UserService userService;

    private final CommentService commentService;


    /**
     * 내가 작성한 게시글들
     * @param token
     * @param pageable
     * @return
     */
    @GetMapping("/board/my")
    @ApiOperation(value = "본인이 작성한 게시글 목록을 리턴", notes = "")
    public ResponseEntity getMyBoard(@RequestHeader(value = "Authorization") String token,
                                        @PageableDefault(size = 50, sort ="id",  direction = Sort.Direction.DESC) Pageable pageable){
        // 유저 정보
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);

        List<Board> boards = boardService.getMyBoard(userSeq, pageable);
        List<BoardResponseDto> result = boards.stream().map(x -> new BoardResponseDto(x)).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    /**
     * 전체 게시글 list 가져오기
     * @return List<BoardDto>
     * 반환 코드 : 200, 204, 404
     */
    @GetMapping("/board/list")
    @ApiOperation(value = "현재 존재하는 모든 게시글 목록을 리턴", notes = "")
    public ResponseEntity<List<BoardResponseDto>> getBoardList(HttpServletRequest request){
        List<Board> boardList = boardService.getBoardList();
        if (boardList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }

        String token = request.getHeader("Authorization");
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            List<BoardResponseDto> result = boardList.stream().map(b -> new BoardResponseDto(b)).collect(Collectors.toList());
            return ResponseEntity.ok().body(result);
        } else {
            Long userSeq = jwtTokenProvider.getUserSeq(token);
            User user = userService.getUserByUserseq(userSeq);
            List<BoardResponseDto> result = boardList.stream().map(b -> new BoardResponseDto(b, user)).collect(Collectors.toList());
            return ResponseEntity.ok().body(result);
        }
    }

    /**
     * 게시글 정보 가져오기
     * @param boardId
     * @return board
     * 반환 코드 : 200, 404
     */
    @GetMapping("/board/{boardId}")
    @ApiOperation(value = "boardId에 해당하는 게시글 정보를 리턴", notes = "")
    public ResponseEntity<?> getBoard(@PathVariable("boardId") Long boardId,
                                      HttpServletRequest request){

        Board board = boardService.getBoard(boardId);

        String token = request.getHeader("Authorization");
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.OK).body(new BoardResponseDto(board));
        } else {
            Long userSeq = jwtTokenProvider.getUserSeq(token);
            User user = userService.getUserByUserseq(userSeq);
            return ResponseEntity.status(HttpStatus.OK).body(new BoardResponseDto(board, user));
        }
    }

    /**
     * 게시글 추가하기
     * @param token
     * @param board : {title, content}
     * @return null
     * 반환 코드 : 201, 403, 404
     */
    @PostMapping("/board")
    @ApiOperation(value = "게시글 작성하기", notes = "")
    public ResponseEntity<?> addBoard(@RequestHeader(value = "Authorization") String token,
                                      @RequestBody BoardRequestDto board){

        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }

        Long userSeq = jwtTokenProvider.getUserSeq(token);

        User user = userService.getUserByUserseq(userSeq);

        Board boardResult = boardService.addBoard(userSeq, board);


        userInfoService.userInfoUpdateBoard(userSeq);
        List<UserBadge> userBadges = badgeService.getBadgeList(userSeq);
        List<Long> badges = badgeService.getUserBadgeSeq(userBadges);
        // 배지갱신
        List<Badge> badgeResult = badgeService.checkBoardUpload(userSeq, badges);
        if(badgeResult == null || badgeResult.size() == 0) return ResponseEntity.ok().body(new BoardResponseDto(boardResult, null, user));
        List<SimpleBadgeDto> result = badgeResult.stream().map(b -> new SimpleBadgeDto(b)).collect(Collectors.toList());

        // 뱃지 확인
        return ResponseEntity.status(HttpStatus.CREATED).body(new BoardResponseDto(boardResult, result, user));
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
    @ApiOperation(value = "게시글 수정하기", notes = "")
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
    @ApiOperation(value = "boardId에 해당하는 게시글 삭제하기", notes = "")
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
    @ApiOperation(value = "해당 게시글에 좋아요를 눌렀는지 여부를 리턴", notes = "")
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
    @ApiOperation(value = "해당 게시글에 좋아요 추가하기", notes = "")
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
    @ApiOperation(value = "해당 게시글에 좋아요를 해제하기", notes = "")
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
    @ApiOperation(value = "본 사용자가 좋아요한 게시글 목록을 리턴", notes = "")
    public ResponseEntity<?> getPlayroomLiked(@RequestHeader(value = "Authorization") String token)
    {
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);

        User user = userService.getUserByUserseq(userSeq);

        List<Board> boards = boardService.getLikedBoards(userSeq);

        List<BoardResponseDto> result = boards.stream().map(b -> new BoardResponseDto(b,user)).collect(Collectors.toList());

        return ResponseEntity.ok().body(result);
    }

    /**
     * 게시글의 덧글 리스트 출력
     * @param boardId
     * @return List<comment>
     * 반환 코드 : 200, 204, 404
     */
    @GetMapping("/board/{boardId}/comment")
    @ApiOperation(value = "해당 게시글의 덧글 목록을 리턴", notes = "")
    public ResponseEntity<List<CommentDto>> getCommentList(@PathVariable("boardId") Long boardId)
    {

        List<Comment> commentList = commentService.getCommentList(boardId);

        if (commentList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }

        List<CommentDto> result = commentList.stream().map(b -> new CommentDto(b)).collect(Collectors.toList());

        return ResponseEntity.ok().body(result);
    }

    /**
     * 게시글 추가하기
     * @param token
     * @param boardId
     * @param comment
     * @return null
     *반환 코드 : 201, 403, 404
     */
    @PostMapping("/board/{boardId}/comment")
    @ApiOperation(value = "해당 게시글에 댓글을 작성하기", notes = "")
    public ResponseEntity<?> addComment(@RequestHeader(value = "Authorization") String token,
                                        @PathVariable("boardId") Long boardId,
                                        @RequestBody Comment comment){
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }

        Long userSeq = jwtTokenProvider.getUserSeq(token);

        Comment commentResult = commentService.addComment(userSeq, boardId, comment);

        return ResponseEntity.status(HttpStatus.CREATED).body(new SimpleCommentDto(commentResult));
    }

    /**
     * 게시글 댓글 갱신하기
     * @param token
     * @param commentId
     * @param comment : {content}
     * @return null
     * 반환 코드 : 200, 401, 403, 404
     */
    @PutMapping("/board/{commentId}/comment")
    @ApiOperation(value = "댓글 수정하기", notes = "")
    public ResponseEntity<?> updateComment(@RequestHeader(value = "Authorization") String token,
                                           @PathVariable("commentId") Long commentId,
                                           @RequestBody Comment comment){

        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }

        Long userSeq = jwtTokenProvider.getUserSeq(token);

        Comment commentSaved = commentService.updateComment(userSeq, commentId, comment);

        if(commentSaved == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    /**
     * 게시글 댓글 지우기
     * @param token
     * @param commentId
     * @return null
     * 반환 코드 : 200, 401, 403, 404
     */
    @DeleteMapping("/board/{commentId}/comment")
    @ApiOperation(value = "댓글 지우기", notes = "")
    public ResponseEntity<?> deleteComment(@RequestHeader(value = "Authorization") String token,
                                           @PathVariable("commentId") Long commentId) {

        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);

        Long result = commentService.deleteComment(commentId, userSeq);

        if(result == -1L){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
