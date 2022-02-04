package hotsixturtles.tupli.service.pubsub;

import com.fasterxml.jackson.databind.ObjectMapper;
import hotsixturtles.tupli.dto.chat.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

/**
 * Redis 발행 서비스 구현
 * Redis에서 메시지가 발행(publish)되면 해당 메시지를 읽어 처리하는 리스너
 * 발행 기능은 RedisPublisher에서 RedisConfig로 이원(redis에서 발행된 데이터를 받아 deserialize하는 작업이 없어짐)하여 일원화함
 * MessageListener를 상속받아 onMessage 메서드를 재작성
 * Topic : 채팅방
 * pub : 대화하기
 * sub : 대화보기
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber {

    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messagingTemplate;

    /**
     * Redis에서 메시지가 발행(publish)되면 대기하고 있던 Redis Subscriber가 해당 메시지를 받아 처리
     */
    public void sendMessage(String message) {
        try {
            // ChatMessage 객채로 맵핑
            ChatMessage chatMessage = objectMapper.readValue(message, ChatMessage.class);
            // Websocket 구독자에게 채팅 메시지 Send
            messagingTemplate.convertAndSend("/sub/chat/room/" + chatMessage.getRoomId(), chatMessage);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
