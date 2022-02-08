package hotsixturtles.tupli.service;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import hotsixturtles.tupli.dto.noti.NotificationDto;
import hotsixturtles.tupli.entity.User;
import hotsixturtles.tupli.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotificationService {

    private final UserRepository userRepository;

    /**
     * 팔로우시 알림
     */
    public void notiFollow(Long from_userSeq, Long to_userSeq) {
        User fromUser = userRepository.findByUserSeq(from_userSeq);
        User toUser = userRepository.findByUserSeq(to_userSeq);

        String alarmType = "follow";
        LocalDateTime curDateTime = LocalDateTime.now();
        String nowDate = curDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));

        // 저장할 데이터
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setType(alarmType);
        notificationDto.setCreatedAt(nowDate);
        notificationDto.setTo(toUser.getNickname());
        notificationDto.setToId(String.valueOf(to_userSeq));
        notificationDto.setFrom(fromUser.getNickname());
        notificationDto.setFromId(String.valueOf(from_userSeq));  // 라우터용 userSeq
        notificationDto.setImage(fromUser.getProfileImage());
        notificationDto.setIsRead("false");

        // 알림 개인 확인용
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("tupli"); // 최상위 root
        String toId = String.valueOf(to_userSeq); // 받는 사람 아이디 seq로 DB에 저장

        DatabaseReference notiRef = ref.child(toId); // 알림 받는 사람의 닉네임
        DatabaseReference nextNotiRef = notiRef.push(); // 다음 키 값으로 푸시
        String postId = nextNotiRef.getKey(); // 현재 알람의1 키값을 가져옴 (덮어쓰기, 중복 방지)
        DatabaseReference saveNoti = notiRef.child(postId); // to의 아이디 값의 child node
        saveNoti.setValueAsync(notificationDto);

        // 실시간 알림용
        final FirebaseDatabase rtdatabase = FirebaseDatabase.getInstance();
        DatabaseReference rtref = rtdatabase.getReference("tupli").child("realtime");
        rtref.setValueAsync(notificationDto);
    }

    /**
     * 플레이룸 개설시 알림
     */
    public void notiPlayroomMake(Long from_userSeq, Long to_userSeq) {
        User fromUser = userRepository.findByUserSeq(from_userSeq);
        User toUser = userRepository.findByUserSeq(to_userSeq);

        String alarmType = "playroomMake";
        LocalDateTime curDateTime = LocalDateTime.now();
        String nowDate = curDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));

        // 저장할 데이터
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setType(alarmType);
        notificationDto.setCreatedAt(nowDate);
        notificationDto.setTo(toUser.getNickname());
        notificationDto.setToId(String.valueOf(to_userSeq));
        notificationDto.setFrom(fromUser.getNickname());
        notificationDto.setFromId(String.valueOf(from_userSeq));  // 라우터용 userSeq
        notificationDto.setImage(fromUser.getProfileImage());
        notificationDto.setIsRead("false");

        // 알림 개인 확인용
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("tupli"); // 최상위 root
        String toId = String.valueOf(to_userSeq); // 받는 사람 아이디 seq로 DB에 저장

        DatabaseReference notiRef = ref.child(toId); // 알림 받는 사람의 닉네임
        DatabaseReference nextNotiRef = notiRef.push(); // 다음 키 값으로 푸시
        String postId = nextNotiRef.getKey(); // 현재 알람의1 키값을 가져옴 (덮어쓰기, 중복 방지)
        DatabaseReference saveNoti = notiRef.child(postId); // to의 아이디 값의 child node
        saveNoti.setValueAsync(notificationDto);

        // 실시간 알림용
        final FirebaseDatabase rtdatabase = FirebaseDatabase.getInstance();
        DatabaseReference rtref = rtdatabase.getReference("tupli").child("realtime");
        rtref.setValueAsync(notificationDto);
    }

    /**
     * 플레이룸 초대시 알림
     */
    public void notiInvite(Long from_userSeq, Long to_userSeq) {
        User fromUser = userRepository.findByUserSeq(from_userSeq);
        User toUser = userRepository.findByUserSeq(to_userSeq);

        String alarmType = "invite";
        LocalDateTime curDateTime = LocalDateTime.now();
        String nowDate = curDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));

        // 저장할 데이터
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setType(alarmType);
        notificationDto.setCreatedAt(nowDate);
        notificationDto.setTo(toUser.getNickname());
        notificationDto.setToId(String.valueOf(to_userSeq));
        notificationDto.setFrom(fromUser.getNickname());
        notificationDto.setFromId(String.valueOf(from_userSeq));  // 라우터용 userSeq
        notificationDto.setImage(fromUser.getProfileImage());
        notificationDto.setIsRead("false");

        // 알림 개인 확인용
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("tupli"); // 최상위 root
        String toId = String.valueOf(to_userSeq); // 받는 사람 아이디 seq로 DB에 저장

        DatabaseReference notiRef = ref.child(toId); // 알림 받는 사람의 닉네임
        DatabaseReference nextNotiRef = notiRef.push(); // 다음 키 값으로 푸시
        String postId = nextNotiRef.getKey(); // 현재 알람의1 키값을 가져옴 (덮어쓰기, 중복 방지)
        DatabaseReference saveNoti = notiRef.child(postId); // to의 아이디 값의 child node
        saveNoti.setValueAsync(notificationDto);

        // 실시간 알림용
        final FirebaseDatabase rtdatabase = FirebaseDatabase.getInstance();
        DatabaseReference rtref = rtdatabase.getReference("tupli").child("realtime");
        rtref.setValueAsync(notificationDto);
    }



    public void realtimeNotiReset() {
        // 실시간 알림 내용 초기화(읽음)
        final FirebaseDatabase rtdatabase = FirebaseDatabase.getInstance();
        DatabaseReference rtref = rtdatabase.getReference("tupli").child("realtime");
        rtref.setValueAsync(null);
    }


    /**
     * 테스트용
     * JUNIT5에서 동작하지 않고 포스트맨에서 동작하는 성질때문에 이렇게 설정함
     */
    public void notiTest() {
        Long to_userSeq = 122L;
        Long from_userSeq = 37L;
        User fromUser = userRepository.findByUserSeq(from_userSeq);
        User toUser = userRepository.findByUserSeq(to_userSeq);

        String alarmType = "follow";
        LocalDateTime curDateTime = LocalDateTime.now();
        String nowDate = curDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));

        // 저장할 데이터
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setType(alarmType);
        notificationDto.setCreatedAt(nowDate);
        notificationDto.setTo(toUser.getNickname());
        notificationDto.setToId(String.valueOf(to_userSeq));
        notificationDto.setFrom(fromUser.getNickname());
        notificationDto.setFromId(String.valueOf(from_userSeq));  // 라우터용 userSeq
        notificationDto.setImage(fromUser.getProfileImage());
        notificationDto.setIsRead("false");

        // 알림 개인 확인용
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("tupli"); // 최상위 root
        String toId = String.valueOf(to_userSeq); // 받는 사람 아이디 seq로 DB에 저장

        DatabaseReference notiRef = ref.child(toId); // 알림 받는 사람의 닉네임
        DatabaseReference nextNotiRef = notiRef.push(); // 다음 키 값으로 푸시
        String postId = nextNotiRef.getKey(); // 현재 알람의1 키값을 가져옴 (덮어쓰기, 중복 방지)
        DatabaseReference saveNoti = notiRef.child(postId); // to의 아이디 값의 child node
        saveNoti.setValueAsync(notificationDto);

        // 실시간 알림용
        final FirebaseDatabase rtdatabase = FirebaseDatabase.getInstance();
        DatabaseReference rtref = rtdatabase.getReference("tupli").child("realtime");
        rtref.setValueAsync(notificationDto);
    }

}
