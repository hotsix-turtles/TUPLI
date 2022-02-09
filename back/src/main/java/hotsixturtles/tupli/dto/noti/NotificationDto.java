package hotsixturtles.tupli.dto.noti;

import lombok.Data;

@Data
public class NotificationDto {

    private String type; // 알림 타입 : 팔로우, 초대, 플레이룸 생성

    private String to;  // 대상
    private String toId; // front 비교용
    private String from; // 알림 출처
    private String fromId; // 출처 대상 라우터용
    private String image; // 알림 관련 사진 ()
    private String createdAt; // 아마 이걸로 나열 하거나 하겠지 싶음
    private String isRead;

}
