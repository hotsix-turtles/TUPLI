package hotsixturtles.tupli.api.dev;

import hotsixturtles.tupli.dto.response.ErrorResponse;
import hotsixturtles.tupli.entity.User;
import hotsixturtles.tupli.service.UserService;
import hotsixturtles.tupli.service.token.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class devApiController {

    private final JwtTokenProvider jwtTokenProvider;
    private final MessageSource messageSource;

    private final UserService userService;

    @GetMapping("/dev/admin")
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
}
