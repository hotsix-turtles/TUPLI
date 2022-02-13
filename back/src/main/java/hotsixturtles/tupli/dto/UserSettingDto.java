package hotsixturtles.tupli.dto;

import hotsixturtles.tupli.entity.UserSetting;
import lombok.Data;

@Data
public class UserSettingDto {

    private Boolean alarmSetting;
    private Boolean alarmOnRealtime;
    private Boolean alarmOnInvite;
    private String inviteDomain;
    private Boolean alarmOnPlayroomMake;
    private Boolean alarmOnBadge;

    public UserSettingDto(UserSetting userSetting) {
        this.alarmSetting = userSetting.getAlarmSetting();
        this.alarmOnRealtime = userSetting.getAlarmOnRealtime();
        this.alarmOnInvite = userSetting.getAlarmOnInvite();
        this.inviteDomain = userSetting.getInviteDomain();
        this.alarmOnPlayroomMake = userSetting.getAlarmOnPlayroomMake();
        this.alarmOnBadge = userSetting.getAlarmOnBadge();
    }
}
