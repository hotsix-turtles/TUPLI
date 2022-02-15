package hotsixturtles.tupli.service;

import hotsixturtles.tupli.entity.User;
import hotsixturtles.tupli.entity.UserSetting;
import hotsixturtles.tupli.entity.likes.UserDislikes;
import hotsixturtles.tupli.entity.likes.UserLikes;
import hotsixturtles.tupli.entity.meta.UserInfo;
import hotsixturtles.tupli.repository.*;
import hotsixturtles.tupli.service.token.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


@Service
@Transactional(readOnly = true)  // 기본적으로 트랜잭션 안에서만 데이터 변경하게 설정(그만큼 최적화 되어 읽는게 빨라짐)
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;
    private final UserLikesRepository userLikesRepository;
    private final UserSettingRepository userSettingRepository;
    private final UserBadgeRepository userBadgeRepository;
    private final UserDislikesRepository userDislikesRepository;
    private final NotificationService notificationService;

    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public Long join(User user) {
        validateDuplicateUser(user);
        user.setPassword(user.getPassword());
        user.encodePassword(passwordEncoder);
        int randNum = (int)(Math.random()*20) + 1;
        user.setProfileImage("#" + randNum);
        userRepository.save(user);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserSeq(user.getUserSeq());
        userInfoRepository.save(userInfo);
        UserSetting userSetting = new UserSetting();
        userSetting.setUser(user);
        userSettingRepository.save(userSetting);
        return user.getUserSeq();
    }

    @Transactional
    public void deleteUser(Long userSeq) {

        userRepository.deleteByUserSeq(userSeq);
        userInfoRepository.deleteByUserSeq(userSeq);
        userBadgeRepository.deleteAllByUserSeq(userSeq);
    }

    @Transactional
    public int updateEmailValidate(String email, String authKey) {
        User user = userRepository.findByEmail(email);
        if (user == null || !user.getAuthKey().equals(authKey)) {
            return -1;
        }
        user.setEmailVerifiedYn("Y");
        userRepository.save(user);

        return 0;
    }


    @Transactional
    public void updateAuthkey(String email, String authKey) {
        User user = userRepository.findByEmail(email);
        user.setAuthKey(authKey);
        userRepository.save(user);
    }

    private void validateDuplicateUser(User user) {
        User findUsers = userRepository.findByEmail(user.getEmail());
        if (findUsers != null) {
            throw new IllegalStateException("일치하는 아이디가 존재합니다.");
        }
    }

    @Transactional
    public User getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        return user;
    }

    // 22.02.02 한길 - userName 으로 user정보 알아오기
    @Transactional
    public User getUserByUserName(String userName) {
        User user = userRepository.findByUsername(userName);
        return user;
    }

    public User getUserByUserseq(Long userSeq){
        return userRepository.findByUserSeq(userSeq);
    }

    // OAUTH용
    public User getUser(String userId) {
        return userRepository.findByUserId(userId);
    }

    // 나중에 parameter 더 넘어오면 변경값들도 바꾼다
    @Transactional
    public void updateProfile(Long userSeq, String introduction, String nickname, String image) {
        User user = userRepository.findByUserSeq(userSeq);
        if (introduction != null) {
            user.setIntroduction(introduction);
        }
        if (nickname != null) {
            user.setNickname(nickname);
        }
        user.setModifiedAt(LocalDateTime.now());

        System.out.println("image = " + image);
        if (image != "") {
            user.setProfileImage(image);
        }

        try {
            userRepository.save(user);
        } catch (Exception e) {
            // 바꾼값이 DB에 이미 있으면 Error!! ( 같으면 안되는 attribute들... )
        }
    }

    // 22.02.02 한길 - follow 했는지 안했는지 정보 반환
    public UserLikes getFollow(Long userSeq, Long otherUserSeq) {
        return userLikesRepository.findExist(userSeq, otherUserSeq);
    }

    // 22.02.02 한길 - follow 하기, 이미 follow 되어있으면 아무일도 하지않음.
    @Transactional
    public void follow(Long userSeq, Long otherUserSeq) {
        UserLikes existUserLikes = userLikesRepository.findExist(userSeq, otherUserSeq);
        if(existUserLikes == null) {
            UserLikes userLikes = new UserLikes();
            userLikes.setFromUser(userRepository.findById(userSeq).orElse(null));
            userLikes.setToUser(userRepository.findById(otherUserSeq).orElse(null));
            userLikesRepository.save(userLikes);

            // 팔로우 알림 보내기
            notificationService.notiFollow(userSeq, otherUserSeq);
        }
        else {
            // 이미 팔로우 되어있음.
        }
    }

    // 22.02.02 한길 - unfollow 하기, 이미 unfollow 되어있으면 아무일도 하지않음.
    @Transactional
    public void unfollow(Long userSeq, Long otherUserSeq) {
        UserLikes existUserLikes = userLikesRepository.findExist(userSeq, otherUserSeq);
        if(existUserLikes != null) {
            userLikesRepository.delete(existUserLikes);
        }
        else {
            // 이미 언팔로우 되어있음.
        }
    }

    ///////////////////////////

    // 22.02.02 한길 - follow 했는지 안했는지 정보 반환
    public UserDislikes getDislike(Long userSeq, Long otherUserSeq) {
        return  userDislikesRepository.findExist(userSeq, otherUserSeq);
    }

    // 22.02.02 한길 - follow 하기, 이미 follow 되어있으면 아무일도 하지않음.
    @Transactional
    public void dislike(Long userSeq, Long otherUserSeq) {
        UserDislikes existUserDislikes = userDislikesRepository.findExist(userSeq, otherUserSeq);
        if(existUserDislikes == null) {
            UserDislikes userDislikes = new UserDislikes();
            userDislikes.setFromUser(userRepository.findById(userSeq).orElse(null));
            userDislikes.setToUser(userRepository.findById(otherUserSeq).orElse(null));
            userDislikesRepository.save(userDislikes);
        }
        else {
            // 이미 팔로우 되어있음.
        }
    }

    // 22.02.02 한길 - unfollow 하기, 이미 unfollow 되어있으면 아무일도 하지않음.
    @Transactional
    public void undoDislike(Long userSeq, Long otherUserSeq) {
        UserDislikes existUserDislikes = userDislikesRepository.findExist(userSeq, otherUserSeq);
        if(existUserDislikes != null) {
            userDislikesRepository.delete(existUserDislikes);
        }
        else {
            // 이미 언팔로우 되어있음.
        }
    }

    ///////////////////////////

    // 22.02.02 한길 - 팔로워 리턴하기
    public List<UserLikes> getFollowers(Long otherUserSeq) {
        // to_user_id 가 otherUserSeq 로 이루어져있는 녀석들만 골라서 저장하고 리턴
        List<UserLikes> userlikes = userLikesRepository.findFollowers(otherUserSeq);
        return userlikes;
    }

    public List<UserLikes> getFollowees(Long otherUserSeq) {
        // to_user_id 가 otherUserSeq 로 이루어져있는 녀석들만 골라서 저장하고 리턴
        List<UserLikes> userlikes = userLikesRepository.findByFromUser(otherUserSeq);
        return userlikes;
    }

    @Transactional
    public void rankUpPremium(String token) {
        User user = jwtTokenProvider.getUser(token);
        user.setIs_vip("Y");
        userRepository.save(user);
    }

    @Transactional
    public void changePassword(User user, String passwordChange) {
        user.setPassword(passwordChange);
        user.encodePassword(passwordEncoder);

        userRepository.save(user);

    }

    public int getFollowersCount(Long userSeq){
        return userLikesRepository.findFollowersCount(userSeq);
    }

    public List<String> getProfileTaste(Long userSeq) {
        User user = userRepository.findByUserSeq(userSeq);
        if (user != null) {
            return user.getTaste();
        } else {
            throw new IllegalStateException("해당 유저 없음");
        }
    }


    public ConcurrentHashMap<String, Integer> getProfileTasteInfo(Long userSeq) {
        UserInfo userInfo = userInfoRepository.findOneByUserSeq(userSeq);
        if (userInfo != null) {
             return userInfo.getTasteInfo();
        } else {
            throw new IllegalStateException("해당 유저 없음");
        }

    }
}

