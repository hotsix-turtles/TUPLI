package hotsixturtles.tupli.api;

import hotsixturtles.tupli.dto.PlayroomDto;
import hotsixturtles.tupli.entity.Playroom;
import hotsixturtles.tupli.service.PlayroomService;
import hotsixturtles.tupli.service.token.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PlayroomApiController {

    private final PlayroomService playroomService;

    private final JwtTokenProvider jwtTokenProvider;

    private final MessageSource messageSource;

    /**
     * 플레이룸 리스트 출력
     * @return
     * 반환 코드 : 200, 404
     */
    @GetMapping("/playroom/list")
    public ResponseEntity<?> getPlayroomList(){

        List<Playroom> playroomList = playroomService.getPlayroomList();

        List<PlayroomDto> result = playroomList.stream().map(b -> new PlayroomDto(b)).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    /**
     * 플레이룸 정보 가져오기
     * @param playroomId
     * @return
     * 반환 코드: 200, 404
     */

    @GetMapping("/playroom/{playroomId}")
    public ResponseEntity<?> getPlayroom(@PathVariable("playroomId") Long playroomId){

        Playroom playroom = playroomService.getPlayroom(playroomId);
        if(playroom == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(new PlayroomDto(playroom));
    }

}
