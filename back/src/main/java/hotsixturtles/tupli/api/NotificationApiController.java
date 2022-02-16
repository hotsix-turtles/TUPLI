package hotsixturtles.tupli.api;

import hotsixturtles.tupli.service.NotificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "알림 관련 API")
public class NotificationApiController {

    private final NotificationService notificationService;

    /**
     * 실시간 알림 후 RTDB 내용 지움
     */
    @GetMapping("/noti/reset")
    @ApiOperation(value = "실시간 알림을 전송 후, RTDB 데이터 삭제", notes = "")
    public void notiReset() {
        notificationService.realtimeNotiReset();
    }

    /**
     * 해당유저의 알림 하나 읽음
     * @param id
     * @param notiId
     */
    @GetMapping("/noti/read/{id}/id/{notiId}")
    @ApiOperation(value = "유저가 알림을 읽었고 읽었음을 체크할 때 실행", notes = "")
    public void notiRead(@PathVariable("id") String id,
                        @PathVariable("notiId") String notiId) {
        notificationService.notiRead(id, notiId);
    }

    /**
     * 해당 유저의 모든 알림 읽음
     * @param id
     */
    @GetMapping("/noti/readAll/{id}")
    @ApiOperation(value = "유저가 모든 알림 읽기를 실행했을 경우 실행", notes = "")
    public void notiReadAll(@PathVariable("id") String id) {
        notificationService.notiReadAll(id);
    }

    // Test 생성 코드
    @GetMapping("/saveAlarm")
    @ApiOperation(value = "알림 테스트용 API", notes = "")
    public ResponseEntity<?> saveAlarm() {
        notificationService.notiTest();
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
