package hotsixturtles.tupli.service;

import hotsixturtles.tupli.dto.UserSettingDto;
import hotsixturtles.tupli.entity.User;
import hotsixturtles.tupli.entity.UserSetting;
import hotsixturtles.tupli.repository.UserRepository;
import hotsixturtles.tupli.repository.UserSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserSettingService {

    private final UserRepository userRepository;
    private final UserSettingRepository userSettingRepository;

    @Transactional
    public void changeSetting(Long userSeq, UserSettingDto userSettingDto) {
        User user = userRepository.findByUserSeq(userSeq);
        UserSetting userSetting = userSettingRepository.findByUser(user);
        if (userSetting!=null) {
            userSetting.setAlarmSetting(userSettingDto.getAlarmSetting());
            userSetting.setAlarmOnRealtime(userSettingDto.getAlarmOnRealtime());
            userSetting.setAlarmOnInvite(userSettingDto.getAlarmOnInvite());
            userSetting.setInviteDomain(userSettingDto.getInviteDomain());
            userSetting.setAlarmOnPlayroomMake(userSettingDto.getAlarmOnPlayroomMake());
            userSetting.setAlarmOnBadge(userSettingDto.getAlarmOnBadge());
        }
    }

    public UserSetting getSetting(Long userSeq) {
        User user = userRepository.findByUserSeq(userSeq);
        return userSettingRepository.findByUser(user);
    }
}
