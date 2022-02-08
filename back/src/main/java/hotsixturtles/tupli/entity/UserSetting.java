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
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String inviteDomain; // (모든사람,팔로워,맞팔로워)

    private Boolean inviteSetting;

    private Boolean AlarmTypeFollow;

    private Boolean AlarmTypeInvite;

    private Boolean AlarmTypePlayroomMake;

    private Boolean AlarmTypeRecommend;

}
