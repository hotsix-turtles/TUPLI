package hotsixturtles.tupli.dto.noti;

import lombok.Data;

@Data
public class NotificationDto {

    private String type; // 알림 타입

    private String from; // follow: 팔로우 누른 사람의 닉네임
    private String fromProfileImage; // 알림을 보낸 사람의 프로필 사진
    private String to;  // 알림 받을 사람????
    private String createdAt;  // 아마 이걸로 나열 하거나 하겠지 싶음
    private Boolean isRead;  // 천재적

//    private String commentId; // comment: 방금 단 댓글의 id (알림 삭제 위함)
//    private String likeId; // like: 방금 한 좋아요 id
//    private String followId; // follow: 방금 신청한 follow id
//    private String userEmail;


    public NotificationDto() {
        this.type = "팔로우";
        this.from = "내가 팔로우를 집도한다.";
        this.fromProfileImage = "hankil.jpg";
        this.to = "이거 실시간으로 되나요?";
        this.createdAt = "2022.02.03";
        this.isRead = false;
    }
}
