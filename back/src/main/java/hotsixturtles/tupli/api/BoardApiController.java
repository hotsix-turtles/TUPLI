package hotsixturtles.tupli.api;

import hotsixturtles.tupli.dto.BoardDto;
import hotsixturtles.tupli.dto.response.ErrorResponse;
import hotsixturtles.tupli.entity.Board;
import hotsixturtles.tupli.service.BoardService;
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
public class BoardApiController {

    private final BoardService boardService;

    private final JwtTokenProvider jwtTokenProvider;

    private final MessageSource messageSource;


    // 전체 board List 가져오기

    /**
     * 전체 게시글 list 가져오기
     * @return List<BoardDto>
     * 반환 코드 : 200, 204, 404
     */
    @GetMapping("/board/list")
    public ResponseEntity<List<BoardDto>> getBoardList(){
        List<Board> boardList = boardService.getBoardList();
        if (boardList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }

        List<BoardDto> result = boardList.stream().map(b -> new BoardDto(b)).collect(Collectors.toList());
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
        return ResponseEntity.status(HttpStatus.OK).body(new BoardDto(board));
    }

    /**
     *
     * @param token
     * @param board : {title, content}
     * @return null
     * 반환 코드 : 201, 403, 404
     */

    @PostMapping("/board")
    public ResponseEntity<?> addBoard(@RequestHeader(value = "AUTH") String token,
                                      @RequestBody Board board){

        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }

        Long userSeq = jwtTokenProvider.getUserSeq(token);

        Board boardResult = boardService.addBoard(userSeq, board);

        return ResponseEntity.status(HttpStatus.CREATED).body(new BoardDto(boardResult));
    }

    /**
     *
     * @param token
     * @param boardId
     * @param board
     * @return null
     * 반환 코드 : 200, 404
     */
    @PutMapping("/board/{boardId}")
    public ResponseEntity<?> updateBoard(@RequestHeader(value = "AUTH") String token,
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
     *
     * @param token
     * @param boardId
     * @return null
     * 반환 코드 : 200, 403, 404
     */
    @DeleteMapping("/board/{boardId}")
    public ResponseEntity<?> deleteBoard(@RequestHeader(value = "AUTH") String token, @PathVariable("boardId") Long boardId){

        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }

        boardService.deleteBoard(boardId);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
