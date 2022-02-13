package hotsixturtles.tupli.api;

import hotsixturtles.tupli.service.NotificationService;
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
public class NotificationApiController {

    private final NotificationService notificationService;

    /**
     * 실시간 알림 후 RTDB 내용 지움
     */
    @GetMapping("/noti/reset")
    public void notiReset() {
        notificationService.realtimeNotiReset();
    }

    /**
     * 해당유저의 알림 하나 읽음
     * @param id
     * @param notiId
     */
    @GetMapping("/noti/read/{id}/id/{notiId}")
    public void notiRead(@PathVariable("id") String id,
                        @PathVariable("notiId") String notiId) {
        notificationService.notiRead(id, notiId);
    }

    /**
     * 해당 유저의 모든 알림 읽음
     * @param id
     */
    @GetMapping("/noti/readAll/{id}")
    public void notiReadAll(@PathVariable("id") String id) {
        notificationService.notiReadAll(id);
    }

    // Test 생성 코드
    @GetMapping("/saveAlarm")
    public ResponseEntity<?> saveAlarm() {
        notificationService.notiTest();
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
