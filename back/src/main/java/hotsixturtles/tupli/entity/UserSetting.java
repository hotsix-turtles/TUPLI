package hotsixturtles.tupli.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "user_setting")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserSetting {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String inviteDomain = "모든사람"; // (모든사람,팔로워,맞팔로워)

    private Boolean inviteSetting = false;

    private Boolean AlarmTypeFollow = false;

    private Boolean AlarmTypeInvite = false;

    private Boolean AlarmTypePlayroomMake = false;

    private Boolean AlarmTypeRecommend = false;

    private Boolean AlarmBadge = false;

}
