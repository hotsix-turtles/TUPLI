package hotsixturtles.tupli.service;

import hotsixturtles.tupli.entity.User;
import hotsixturtles.tupli.entity.meta.UserInfo;
import hotsixturtles.tupli.repository.UserInfoRepository;
import hotsixturtles.tupli.repository.UserRepository;
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
    public void userInfoUpdateTime(Long userSeq, Long time) {
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

    @Transactional
    public void updatePlaylistMake(Long userSeq){
        UserInfo userInfo = userInfoRepository.findOneByUserSeq(userSeq);
        userInfo.setMakePlaylist(userInfo.getMakePlaylist() + 1L);
        userInfoRepository.save(userInfo);
    }
    @Transactional
    public void updatePlayroomMake(Long userSeq){
        UserInfo userInfo = userInfoRepository.findOneByUserSeq(userSeq);
        userInfo.setMakePlayroom(userInfo.getMakePlayroom() + 1L);
        userInfoRepository.save(userInfo);
    }

}
