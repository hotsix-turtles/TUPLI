package hotsixturtles.tupli.service;

import hotsixturtles.tupli.entity.User;
import hotsixturtles.tupli.repository.UserRepository;
import hotsixturtles.tupli.service.token.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)  // 기본적으로 트랜잭션 안에서만 데이터 변경하게 설정(그만큼 최적화 되어 읽는게 빨라짐)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public Long join(User user){
        validateDuplicateUser(user);
        user.setPassword(user.getPassword());
        user.encodePassword(passwordEncoder);

        userRepository.save(user);
        return user.getUserSeq();
    }

    @Transactional
    public void deleteUser(Long userSeq){
        userRepository.deleteByUserSeq(userSeq);
    }

    @Transactional
    public int updateEmailValidate(String email, String authKey){
        User user = userRepository.findByEmail(email);
        if(user == null || !user.getAuthKey().equals(authKey)){
            return -1;
        }
        user.setEmailVerifiedYn("Y");
        userRepository.save(user);

        return 0;
    }


    @Transactional
    public void updateAuthkey(String email, String authKey){
        User user = userRepository.findByEmail(email);
        user.setAuthKey(authKey);
        userRepository.save(user);
    }

    private void validateDuplicateUser(User user) {
        List<User> findUsers = userRepository.findByUsername(user.getUsername());
        if(!findUsers.isEmpty()) {
            throw new IllegalStateException("일치하는 아이디가 존재합니다.");
        }
    }

    @Transactional
    public User getUserByEmail(String email){
        User user = userRepository.findByEmail(email);
        return user;
    }

    // OAUTH용
    public User getUser(String userId) {
        return userRepository.findByUserId(userId);
    }

    // 나중에 parameter 더 넘어오면 변경값들도 바꾼다
    @Transactional
    public void updateProfile(Long userSeq, String email, String nickname, String image){
        User user = userRepository.findByUserSeq(userSeq);
        if (email != null) {
            user.setEmail(email);
        }
        if (nickname != null) {
            user.setNickname(nickname);
        }
        user.setModifiedAt(LocalDateTime.now());

        System.out.println("image = " + image);
        if (image != "") {
            user.setProfileImageUrl(image);
        }

        try {
            userRepository.save(user);
        }
        catch (Exception e){
            // 바꾼값이 DB에 이미 있으면 Error!! ( 같으면 안되는 attribute들... )
        }
    }

}
