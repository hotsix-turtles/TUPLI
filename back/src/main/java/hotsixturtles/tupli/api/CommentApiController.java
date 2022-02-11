package hotsixturtles.tupli.api;

import hotsixturtles.tupli.dto.CommentDto;
import hotsixturtles.tupli.dto.response.ErrorResponse;
import hotsixturtles.tupli.dto.simple.SimpleCommentDto;
import hotsixturtles.tupli.entity.Comment;
import hotsixturtles.tupli.service.CommentService;
import hotsixturtles.tupli.service.token.JwtTokenProvider;
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
public class CommentApiController {

    private final JwtTokenProvider jwtTokenProvider;

    private final MessageSource messageSource;

    private final CommentService commentService;

    /**
     * 게시글의 덧글 리스트 출력
     * @param boardId
     * @return List<comment>
     * 반환 코드 : 200, 204, 404
     */
    @GetMapping("/comment/list/{boardId}")
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
    @PostMapping("/comment/{boardId}")
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
     * @param boardId
     * @param comment : {content}
     * @return null
     * 반환 코드 : 200, 401, 403, 404
     */
    @PutMapping("/comment/{boardId}")
    public ResponseEntity<?> updateComment(@RequestHeader(value = "Authorization") String token,
                                         @PathVariable("boardId") Long boardId,
                                         @RequestBody Comment comment){

        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }

        Long userSeq = jwtTokenProvider.getUserSeq(token);

        Comment commentSaved = commentService.updateComment(userSeq, boardId, comment);

        if(commentSaved == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    /**
     * 게시글 댓글 지우기
     * @param token
     * @param boardId
     * @param comment
     * @return null
     * 반환 코드 : 200, 401, 403, 404
     */
    @DeleteMapping("/comment/{boardId}")
    public ResponseEntity<?> deleteComment(@RequestHeader(value = "Authorization") String token,
                                         @PathVariable("boardId") Long boardId,
                                         @RequestBody Comment comment){

        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);

        Long result = commentService.deleteComment(comment.getId(), userSeq);

        if(result == -1L){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
