package hotsixturtles.tupli.dto.chat;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * REDIS 저장 채팅방 유저 정보
 * key: sessionID
 */
@Data
public class ChatUserInfo implements Serializable {

    public ChatUserInfo() {
    }

    private String roomId; // 방번호
    private String sender; // 유저 이름
    private String userSeq; // 유저 번호

    @Builder
    public ChatUserInfo(String roomId, String sender, String userSeq) {
        this.roomId = roomId;
        this.sender = sender;
        this.userSeq = userSeq;
    }
}
