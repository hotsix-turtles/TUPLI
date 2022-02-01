package hotsixturtles.tupli.api.test;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequiredArgsConstructor
public class TestApiController {

    @GetMapping("/")
    public ResponseEntity home() {
        return ResponseEntity.ok().body("ok");
    }

    @PostMapping("/echo")
    public ResponseEntity postTest(@RequestBody String test) {

        return ResponseEntity.ok().body(test);
    }

    @GetMapping("/echo")
    public ResponseEntity getTest() {

        return ResponseEntity.ok().body("ok");
    }

}
