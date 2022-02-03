package hotsixturtles.tupli.api;

import hotsixturtles.tupli.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class NotificationApiController {

    private final NotificationService notificationService;

    // 어차피 firebase DB에 적히는건데 get으로 해야하나? post로 해야하나?
    @PostMapping("/saveAlarm")
    public ResponseEntity<?> saveAlarm() {
        System.out.println("잘 되나요?");
        notificationService.saveNoti();
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
