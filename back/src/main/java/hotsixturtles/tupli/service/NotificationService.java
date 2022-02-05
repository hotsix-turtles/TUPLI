package hotsixturtles.tupli.service;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import hotsixturtles.tupli.dto.noti.NotificationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotificationService {

    /**
     * 팔로우시 알림
     */
    public void notiFollow() {
        String alarmType = "follow";
        LocalDateTime curDateTime = LocalDateTime.now();
        String nowDate = curDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));

        // 저장할 데이터
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setType(alarmType);
        notificationDto.setCreatedAt(nowDate);
        notificationDto.setTo("mingu4969");  // front 비교용
        notificationDto.setFrom("민구테스터");
        notificationDto.setFromId(String.valueOf(1L));  // 라우터용 userSeq
        notificationDto.setImage("사진주소");
        notificationDto.setIsRead("false");

        // 알림 개인 확인용
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("tupliTest"); // 최상위 root
        String toId = "mingu4969"; //보내는 아이디

        DatabaseReference notiRef = ref.child(toId); // 알림 받는 사람의 닉네임
        DatabaseReference nextNotiRef = notiRef.push(); // 다음 키 값으로 푸시
        String postId = nextNotiRef.getKey(); // 현재 알람의1 키값을 가져옴 (덮어쓰기, 중복 방지)
        DatabaseReference saveNoti = notiRef.child(postId); // to의 아이디 값의 child node
        saveNoti.setValueAsync(notificationDto);

        // 실시간 알림용
        final FirebaseDatabase rtdatabase = FirebaseDatabase.getInstance();
        DatabaseReference rtref = rtdatabase.getReference("tupliTest").child("realtime");
        rtref.setValueAsync(notificationDto);
    }



}
