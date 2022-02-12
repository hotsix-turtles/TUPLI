package hotsixturtles.tupli.api.dev;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * 관리자 API 메아리(echo)
 */
@Controller
@RequiredArgsConstructor
public class TestApiController {

    /**
     * 서버 동작 확인용
     * @return
     */
    @GetMapping("/")
    public ResponseEntity home() {
        return ResponseEntity.ok().body("ok");
    }

    /**
     * POST 동작 확인용 메아리
     * @param test
     * @return
     */
    @PostMapping("/echo")
    public ResponseEntity postTest(@RequestBody String test) {

        return ResponseEntity.ok().body(test);
    }

    /**
     * 서버 동작 확인용
     * @return
     */
    @GetMapping("/echo")
    public ResponseEntity getTest() {

        return ResponseEntity.ok().body("ok");
    }

}
