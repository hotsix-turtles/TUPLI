package hotsixturtles.tupli.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "user_setting")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSetting {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Boolean alarmSetting = true;  // 알림 여부
    private Boolean alarmOnRealtime = true;  // 실시간 알림 받기
    private Boolean alarmOnInvite = true; // 초대 받기
    private String inviteDomain = "everyone"; //초대 가능한 사람 (everyone, followers, co-followers)
    private Boolean alarmOnPlayroomMake = true;  // 플레이룸 생성시 팔로워에게 알람 보낼지 여부
    private Boolean alarmOnBadge = true;  // 뱃지 획득시 알림 여부

}
