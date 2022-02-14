package hotsixturtles.tupli.api.dev;

import hotsixturtles.tupli.dto.response.ErrorResponse;
import hotsixturtles.tupli.entity.User;
import hotsixturtles.tupli.service.BoardService;
import hotsixturtles.tupli.service.PlaylistService;
import hotsixturtles.tupli.service.PlayroomService;
import hotsixturtles.tupli.service.UserService;
import hotsixturtles.tupli.service.token.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 관리자 API : 기본
 * SWAGGER 대상되면 안됨!!!  : ApiIgnore
 * 책임질 수 있는 사람만 관리자 아이디로 기동할 것!!!!!!!!!!!!!!!!!
 */
@ApiIgnore
@RestController
@RequiredArgsConstructor
public class devApiController {

    private final JwtTokenProvider jwtTokenProvider;
    private final MessageSource messageSource;

    private final UserService userService;
    private final BoardService boardService;
    private final PlayroomService playroomService;
    private final PlaylistService playlistService;

    @GetMapping("/admin")
    public ResponseEntity checkAdmin(HttpServletRequest request){
        // 회원, 비회원(유효하지 않은 토큰) 구분
        String token = request.getHeader("Authorization");
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.
                            getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }

        Long userSeq = jwtTokenProvider.getUserSeq(token);

        User user = userService.getUserByUserseq(userSeq);

        if(user.getEmail().trim().equals("hotsixturtles@gmail.com")){
            return ResponseEntity.ok(null);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    /**
     * 유저 강제 탈퇴
     * @param id
     * @param request
     * @return
     */
    @DeleteMapping("/admin/user/{id}")
    public ResponseEntity userDeleteAdmin(@PathVariable("id") Long id,
                                          HttpServletRequest request){
        // 회원, 비회원(유효하지 않은 토큰) 구분
        String token = request.getHeader("Authorization");
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.
                            getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);
        User user = userService.getUserByUserseq(userSeq);
        if(user.getEmail().trim().equals("hotsixturtles@gmail.com")){
            // 관리자 로직, 이 행동에 책임을 질 것. (강민구)
            userService.deleteUser(userSeq);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    /**
     * 게시글 강제 삭제
     * @param id
     * @param request
     * @return
     */
    @DeleteMapping("/admin/board/{id}")
    public ResponseEntity boardDeleteAdmin(@PathVariable("id") Long id,
                                          HttpServletRequest request){
        // 회원, 비회원(유효하지 않은 토큰) 구분
        String token = request.getHeader("Authorization");
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.
                            getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);
        User user = userService.getUserByUserseq(userSeq);
        if(user.getEmail().trim().equals("hotsixturtles@gmail.com")){
            // 관리자 로직, 이 행동에 책임을 질 것. (강민구)
            boardService.deleteBoard(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    /**
     * 플레이룸 강제 삭제
     * @param id
     * @param request
     * @return
     */
    @DeleteMapping("/admin/playroom/{id}")
    public ResponseEntity playroomDeleteAdmin(@PathVariable("id") Long id,
                                         HttpServletRequest request){
        // 회원, 비회원(유효하지 않은 토큰) 구분
        String token = request.getHeader("Authorization");
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.
                            getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);
        User user = userService.getUserByUserseq(userSeq);
        if(user.getEmail().trim().equals("hotsixturtles@gmail.com")){
            // 관리자 로직, 이 행동에 책임을 질 것. (강민구)
            playroomService.deletePlayroom(id, -1L);  // -1L이면 통과
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    /**
     * 플레이리스트 강제 삭제
     * @param id
     * @param request
     * @return
     */
    @DeleteMapping("/admin/playlist/{id}")
    public ResponseEntity playlistDeleteAdmin(@PathVariable("id") Long id,
                                              HttpServletRequest request){
        // 회원, 비회원(유효하지 않은 토큰) 구분
        String token = request.getHeader("Authorization");
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.
                            getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);
        User user = userService.getUserByUserseq(userSeq);
        if(user.getEmail().trim().equals("hotsixturtles@gmail.com")) {
            // 관리자 로직, 이 행동에 책임을 질 것. (강민구)
            playlistService.deletePlaylist(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }


}
