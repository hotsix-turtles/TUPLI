package hotsixturtles.tupli.repository.chat;

import hotsixturtles.tupli.dto.chat.ChatMessage;
import hotsixturtles.tupli.dto.chat.ChatRoom;
import hotsixturtles.tupli.dto.chat.ChatUserInfo;

import hotsixturtles.tupli.service.list.SystemConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * 채팅방과 관련된 데이터를 이 곳(한 군데)에서 처리
 */

@RequiredArgsConstructor
@Repository
public class ChatRoomRepository {

    private final RedisTemplate redisTemplate;

    // Redis CacheKeys
    private static final String CHAT_ROOMS = "CHAT_ROOM"; // 채팅룸 저장
    public static final String ENTER_INFO = "ENTER_INFO"; // 채팅룸에 입장한 클라이언트의 sessionId와 채팅룸 id를 맵핑한 정보 저장
    public static final String USER_COUNT = "USER_COUNT"; // 채팅룸에 입장한 클라이언트수 저장
    public static final String CHAT_LIST = "CHAT_LIST"; // 채팅 룸 메시지 내역(최신 50 저장)
    public static final String ROOM_TTL = "ROOM_TTL"; // room 삭제

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, ChatRoom> hashOpsChatRoom;  // 채팅방 ("CHAT_ROOM", 방 id, chatroom 객체)
    @Resource(name = "redisTemplate")
    private HashOperations<String, String, ChatUserInfo> hashOpsEnterInfo;  // 채팅방 유저 정보("ENTER_INFO", 세션 id, 방 id)
    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> valueOps;  // 채팅방 유저수
    @Resource(name = "redisTemplate")
    private HashOperations<String, String, List<ChatMessage>> roomMessages; // 최근 메시지 저장용
    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> roomTtl; // ttl 만들어보자

    // 모든 채팅방 조회
    public List<ChatRoom> findAllRoom() {
        return hashOpsChatRoom.values(CHAT_ROOMS);
    }

    // 특정 채팅방 조회
    public ChatRoom findRoomById(String id) {
        return hashOpsChatRoom.get(CHAT_ROOMS, id);
    }

    // 채팅방 생성 : 서버간 채팅방 공유를 위해 redis hash에 저장한다.
    public ChatRoom createChatRoom(String name) {
        ChatRoom chatRoom = ChatRoom.create(name);
        hashOpsChatRoom.put(CHAT_ROOMS, chatRoom.getRoomId(), chatRoom);
        roomTtl.set(chatRoom.getRoomId(), "Zzz",24000, SECONDS);
        return chatRoom;
    }

    // 채팅방 삭제 : redis hash에 저장된 채팅방 파괴
    public void deleteChatRoom(String roomId) {
        // 채팅방 정보 삭제
        hashOpsChatRoom.delete(CHAT_ROOMS, roomId);
        // 채팅방 메시지 삭제
        roomMessages.delete(CHAT_LIST, roomId);
        // 채팅방 카운트 삭제
        redisTemplate.delete(USER_COUNT + "_" + roomId);

        // hashOpsEnterInfo(채팅방 유저 정보)에서 채팅방 ID를 value으로 가지는 key를 삭제
        Map<String, ChatUserInfo> entries = hashOpsEnterInfo.entries(ENTER_INFO);
        for (Map.Entry<String, ChatUserInfo> stringStringEntry : entries.entrySet()) {
            if (stringStringEntry.getValue().getRoomId().equals(roomId)) {
                hashOpsEnterInfo.delete(ENTER_INFO, stringStringEntry.getKey());
            }
        }


    }

    // 유저가 입장한 채팅방ID와 유저 세션ID 맵핑 정보 저장
    public void setUserEnterInfo(String sessionId, ChatUserInfo chatUserInfo) {
        hashOpsEnterInfo.put(ENTER_INFO, sessionId, chatUserInfo);
    }

    // 유저 세션으로 입장해 있는 채팅방 ID 조회
    public ChatUserInfo getUserEnterRoomId(String sessionId) {
        return hashOpsEnterInfo.get(ENTER_INFO, sessionId);
    }

    // 유저 세션정보와 맵핑된 채팅방ID 삭제
    public void removeUserEnterInfo(String sessionId) {
        hashOpsEnterInfo.delete(ENTER_INFO, sessionId);
    }

    // 채팅방 유저수 조회
    public long getUserCount(String roomId) {
        return Long.valueOf(Optional.ofNullable(valueOps.get(USER_COUNT + "_" + roomId)).orElse("0"));
    }

    // 채팅방에 입장한 유저수 +1
    public long plusUserCount(String roomId) {
        return Optional.ofNullable(valueOps.increment(USER_COUNT + "_" + roomId)).orElse(0L);
    }

    // 채팅방에 입장한 유저수 -1
    public long minusUserCount(String roomId) {
        return Optional.ofNullable(valueOps.decrement(USER_COUNT + "_" + roomId)).filter(count -> count > 0).orElse(0L);
    }

    // 메시지 저장
    public void saveMessage(String roomId, ChatMessage message) {
        List<ChatMessage> messages = roomMessages.get(CHAT_LIST, roomId);
        if(messages == null) messages = new ArrayList<>();
        messages.add(message);

        // 다훈
        if(messages.size() > SystemConstant.MESSAGE_MAX_LENGTH){
            List<ChatMessage> changeMessages = new ArrayList<>(messages.subList(1, SystemConstant.MESSAGE_MAX_LENGTH));

            roomMessages.put(CHAT_LIST, roomId, changeMessages);
            return;
        }
        roomMessages.put(CHAT_LIST, roomId, messages);

        //민구
//        if(messages.size() > SystemConstant.MESSAGE_MAX_LENGTH){
//            messages = messages.stream().skip(0).limit(21).collect(Collectors.toList());
//        }

        // 한길
//        if(messages.size() > SystemConstant.MESSAGE_MAX_LENGTH) {
//            // 앞에서부터1개식 지우기
//            for (int i = 1; i < messages.size(); i++) {
//                messages.set(i-1, messages.get(i));
//            }
//            messages.add(message);
//        }
//        else {
//            // 제거쓸거면 번120째 messages.add(message)는 지워야함
//            messages.add(message);
//        }


    }

    // 저장 메시지 보내기
    public List<ChatMessage> getMessages(String roomId) {
        List<ChatMessage> messages = roomMessages.get(CHAT_LIST, roomId);
        if(messages == null) return new ArrayList<>();
        return messages;
    }
}