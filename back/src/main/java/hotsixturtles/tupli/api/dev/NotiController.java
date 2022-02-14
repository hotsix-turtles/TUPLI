package hotsixturtles.tupli.api.dev;

import hotsixturtles.tupli.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 관리자 API : 알림 제어
 * SWAGGER 대상되면 안됨!!!  : ApiIgnore
 */
@ApiIgnore
@RestController
@RequiredArgsConstructor
public class NotiController {

    private final NotificationService notificationService;

    /**
     * 초대 알림 보내기
     * @param fromId
     * @param toId
     */
    @GetMapping("/noti/invite/from/{fromId}/to/{toId}/playroom/{playroomId}")
    public ResponseEntity notiInvite(@PathVariable("fromId") Long fromId,
                                     @PathVariable("toId") Long toId,
                                     @PathVariable("playroomId") Long playroomId) {
        notificationService.notiInvite(fromId, toId, playroomId);
        return ResponseEntity.ok().body("초청 알림 완료");
    }

    /**
     * 팔로우 알림 보내기
     * @param fromId
     * @param toId
     */
    @GetMapping("/noti/follow/from/{fromId}/to/{toId}")
    public ResponseEntity notiFollow(@PathVariable("fromId") Long fromId,
                                     @PathVariable("toId") Long toId) {
        notificationService.notiFollow(fromId, toId);
        return ResponseEntity.ok().body("팔로우 알림 완료");
    }

    /**
     * 플레이방 개설 알림 보내기
     * @param fromId
     * @param toId
     */
    @GetMapping("/noti/playroom/from/{fromId}/to/{toId}/playroom/{playroomId}")
    public ResponseEntity notiPlayroomMake(@PathVariable("fromId") Long fromId,
                                           @PathVariable("toId") Long toId,
                                           @PathVariable("playroomId") Long playroomId) {
        notificationService.notiPlayroomMake(fromId, toId, playroomId);
        return ResponseEntity.ok().body("플레이룸 개설 알림 완료");
    }


}
