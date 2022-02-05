package hotsixturtles.tupli.service;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import hotsixturtles.tupli.dto.noti.NotificationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotificationService {

    /**
     * 알림 내용 DB에 저장
     */
    public void saveNoti() {
//        LocalDateTime curDateTime = LocalDateTime.now();
//        String nowDate = curDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));

        // 저장할 데이터
        NotificationDto notificationDto = new NotificationDto();
        System.out.println(notificationDto.toString());

//        String type = "민구Test2";
//        notificationDto.setType(type);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("noti"); // 최상위 root: noti // ?? 없으면 만들어주나?
        String toId = "보내는아이디";

        DatabaseReference notiRef = ref.child(toId); // 알림 받는 사람의 닉네임
        DatabaseReference nextNotiRef = notiRef.push(); // 다음 키값으로 푸시
        String postId = nextNotiRef.getKey(); // 현재 알람의1 키값을 가져옴
        DatabaseReference saveNoti = notiRef.child(postId); // to의 아이디 값의 child node

        saveNoti.setValueAsync(notificationDto);

    }



}
