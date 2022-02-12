package hotsixturtles.tupli.api.dev;

import hotsixturtles.tupli.service.FlaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 관리자 API : 플라스크 제어
 * SWAGGER 대상되면 안됨!!!  : ApiIgnore
 */
@ApiIgnore
@Controller
@RequiredArgsConstructor
public class FlaskController {

    private final FlaskService flaskService;

    /**
     * 모든 플레이리스트 추천 갱신
     * @return
     */
    @GetMapping("/flask/playlist/all")
    public ResponseEntity playlistRecommendRenew() {

        // 추천 고도화 (플라스크)
        try {
            flaskService.recommendPlaylistRenew();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(null);
    }

}
