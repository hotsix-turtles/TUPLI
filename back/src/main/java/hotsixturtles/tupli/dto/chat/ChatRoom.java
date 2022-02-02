package hotsixturtles.tupli.dto.chat;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

/**
 * REDIS 저장 채팅방
 * key: roomID
 */
@Data
public class ChatRoom implements Serializable {

    private static final long serialVersionUID = 6494678977089006639L;

    private String roomId;
    private String name;
    private long userCount; // 채팅방 인원수

    public static ChatRoom create(String name) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomId = UUID.randomUUID().toString();
        chatRoom.name = name;
        return chatRoom;
    }

}
