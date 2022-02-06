package hotsixturtles.tupli.service;

import hotsixturtles.tupli.entity.meta.UserInfo;
import hotsixturtles.tupli.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserInfoService {


    private final UserInfoRepository userInfoRepository;

    @Transactional
    public void userInfoUpdate(Long userSeq, Long time) {
        UserInfo userInfo = userInfoRepository.findOneByUserSeq(userSeq);
        if (userInfo == null) {
            userInfo = new UserInfo();
            userInfo.setUserSeq(userSeq);
        }
        userInfo.setWatchTime(userInfo.getWatchTime() + time);
        userInfoRepository.save(userInfo);
    }

    @Transactional
    public void userInfoUpdateBoard(Long userSeq) {
        UserInfo userInfo = userInfoRepository.findOneByUserSeq(userSeq);
        if (userInfo == null) {
            userInfo = new UserInfo();
            userInfo.setUserSeq(userSeq);
        }
        userInfo.setBoardUpload(userInfo.getBoardUpload() + 1L);
        userInfoRepository.save(userInfo);
    }

    @Transactional
    public void deleteUserInfo(Long userSeq){
        UserInfo userinfo = userInfoRepository.findOneByUserSeq(userSeq);
        userInfoRepository.delete(userinfo);
    }

    @Transactional
    public void updateDailyLogin(Long userSeq){
        UserInfo userInfo = userInfoRepository.findOneByUserSeq(userSeq);
        if(userInfo.getDailyLoginYN().equals("N")) {
            userInfo.setDailyLoginYN("Y");
            userInfoRepository.save(userInfo);
        }
    }

}
