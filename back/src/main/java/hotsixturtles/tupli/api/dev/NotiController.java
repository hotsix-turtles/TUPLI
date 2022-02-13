package hotsixturtles.tupli.api.dev;

import hotsixturtles.tupli.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 관리자 API : 플라스크 제어
 * SWAGGER 대상되면 안됨!!!  : ApiIgnore
 */
@ApiIgnore
@Controller
@RequiredArgsConstructor
public class NotiController {

    private final NotificationService notificationService;

    /**
     * 초대 알림 보내기
     * @param fromId
     * @param toId
     */
    @GetMapping("/noti/invite/from/{fromId}/to/{toId}")
    public void notiInvite(@PathVariable("fromId") Long fromId,
                         @PathVariable("toId") Long toId) {
        notificationService.notiInvite(fromId, toId);
    }

    /**
     * 팔로우 알림 보내기
     * @param fromId
     * @param toId
     */
    @GetMapping("/noti/follow/from/{fromId}/to/{toId}")
    public void notiFollow(@PathVariable("fromId") Long fromId,
                         @PathVariable("toId") Long toId) {
        notificationService.notiFollow(fromId, toId);
    }

    /**
     * 플레이방 개설 알림 보내기
     * @param fromId
     * @param toId
     */
    @GetMapping("/noti/playroom/from/{fromId}/to/{toId}")
    public void notiPlayroomMake(@PathVariable("fromId") Long fromId,
                           @PathVariable("toId") Long toId) {
        notificationService.notiPlayroomMake(fromId, toId);
    }


}
