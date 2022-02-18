package hotsixturtles.tupli.service;

import hotsixturtles.tupli.entity.User;
import hotsixturtles.tupli.repository.UserRepository;
import hotsixturtles.tupli.utils.MailUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailSendService {

    private final JavaMailSender mailSender;
    private final UserService userService;
    private final UserRepository userRepository;
    private int size;

    //인증키 생성
    private String getKey(int size) {
        this.size = size;
        return getAuthCode();
    }

    // 인증코드 난수 생성
    private String getAuthCode() {
        Random random = new Random();
        StringBuffer buffer = new StringBuffer();
        int num = 0;

        while(buffer.length() < size) {
            num = random.nextInt(10);
            buffer.append(num);
        }
        return buffer.toString();
    }

    // 인증메일 Send
    public String sendAuthMail(String email) {
        // 6자리 난수 인증번호 생성
        String authKey = getKey(6);

        // 인증메일 보내기
        try {
            MailUtils sendMail = new MailUtils(mailSender);
            sendMail.setSubject("회원가입 이메일 인증");
            sendMail.setText(new StringBuffer().append("<h1>[이메일 인증]</h1>")
                    .append("<p>아래 링크를 클릭하시면 이메일 인증이 완료됩니다.</p>")
//                    .append("<a href='http://localhost:8080/account/signUpConfirm?email=")
                    .append("<a href='http://localhost:3000/handler/mail?email=")
                    .append(email)
                    .append("&authKey=")
                    .append(authKey)
                    .append("' target='_blenk'>이메일 인증 확인</a>")
                    .toString());
            sendMail.setFrom("hotsixturtles@gmail.com", "TUPLI");
            sendMail.setTo(email);
            sendMail.send();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return authKey;
    }

    // 임시 비밀번호
    public void sendTmpPassword(User user, String email) {
        // 12자리 난수 인증번호 생성
        String tempPass = getKey(12);

        // 인증메일 보내기
        try {
            MailUtils sendMail = new MailUtils(mailSender);
            sendMail.setSubject("[Tupli] 임시 비밀번호 발송");
            sendMail.setText(new StringBuffer().append("<h1>[Tupli] 임시 비밀번호 발송</h1>")
                    .append("<p>아래 임시비밀번호로 로그인해주세요.</p>")
                    .append("<p>비밀번호는 프로필-설정에서 변경하실수 있습니다.</p>")
//                    .append("<p>아래 링크를 클릭하시면 이메일 인증이 완료됩니다.</p>")
//                    .append("<a href='http://localhost:8080/account/signUpConfirm?email=")
//                    .append("<a href='http://localhost:3000/handler/mail?email=")  // local
//                    .append("<a href='https://i6a102.p.ssafy.io/handler/mail?email=")  // EC2
                    .append(email)
                    .append("&임시비밀번호=")
                    .append(tempPass)
                    .append("' target='_blenk'>임시비밀번호 발송</a>")
                    .toString());
            sendMail.setFrom("hotsixturtles@gmail.com", "TUPLI");
            sendMail.setTo(email);
            sendMail.send();

            // 실제 비밀번호 변경
            userService.changePassword(user, tempPass);

        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}