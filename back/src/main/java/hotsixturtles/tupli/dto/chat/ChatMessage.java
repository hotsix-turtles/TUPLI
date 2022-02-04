package hotsixturtles.tupli.dto.chat;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * REDIS 저장 채팅 메시지
 * key: roomID
 */
@Data
public class ChatMessage implements Serializable {

    //MismatchedInputException 오류 관련(https://jinseongsoft.tistory.com/232)
    public ChatMessage() {
    }

    // 메시지 타입 : 채팅방 입장, 채팅방 메시지보내기, 채팅방 나가기
    public enum MessageType {
<<<<<<< HEAD
        ENTER, TALK, QUIT, SYNC
=======
        ENTER, TALK, QUIT
>>>>>>> 2e271a7bd18cd87da6b2056108ea373197c120d5
    }
    private MessageType type; // 메시지 타입
    private String roomId; // 방번호
    private String sender; // 메시지 보낸사람
    private String message; // 메시지
    private long userCount;  // 채팅방 인원수, 채팅방 내에서 메시지가 전달될때 인원수 갱신시 사용
<<<<<<< HEAD
    private String img; // 유저 이미지(ENTER 시점에 캐싱)

    
    @Builder
    public ChatMessage(MessageType type, String roomId, String sender, String message, long userCount, String img) {
=======

    @Builder
    public ChatMessage(MessageType type, String roomId, String sender, String message, long userCount) {
>>>>>>> 2e271a7bd18cd87da6b2056108ea373197c120d5
        this.type = type;
        this.roomId = roomId;
        this.sender = sender;
        this.message = message;
        this.userCount = userCount;
<<<<<<< HEAD
        this.img = img;
=======
>>>>>>> 2e271a7bd18cd87da6b2056108ea373197c120d5
    }
    
}
