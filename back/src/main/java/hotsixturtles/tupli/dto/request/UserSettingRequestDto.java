package hotsixturtles.tupli.dto.request;

import lombok.Data;

@Data
public class UserSettingRequestDto {

    private Boolean alarmSetting;
    private Boolean alarmOnRealtime;
    private Boolean alarmOnInvite;
    private String inviteDomain;
    private Boolean alarmOnPlayroomMake;
    private Boolean alarmOnBadge;

}
