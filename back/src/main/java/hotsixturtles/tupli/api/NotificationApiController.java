package hotsixturtles.tupli.api;

import hotsixturtles.tupli.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class NotificationApiController {

    private final NotificationService notificationService;

    @GetMapping("/saveAlarm")
    public ResponseEntity<?> saveAlarm() {
        notificationService.notiFollow();
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
