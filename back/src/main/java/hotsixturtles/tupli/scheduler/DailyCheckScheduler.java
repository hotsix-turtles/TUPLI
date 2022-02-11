package hotsixturtles.tupli.scheduler;

import hotsixturtles.tupli.entity.meta.UserInfo;
import hotsixturtles.tupli.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 매일 0시(서버시간 기준) 출석체크 가능 상태로 변함
 */
@Component
@RequiredArgsConstructor
public class DailyCheckScheduler {

    private final UserInfoRepository userInfoRepository;

    @Scheduled(cron="0 0 0 * * *")
    public void DailyCheck(){
        List<UserInfo> userInfoList = userInfoRepository.findAll();
        for(UserInfo nowUserInfo : userInfoList){
            nowUserInfo.setDailyLoginYN("N");
            userInfoRepository.save(nowUserInfo);
        }
    }
}
