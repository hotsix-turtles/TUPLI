package hotsixturtles.tupli.config.listener;

import hotsixturtles.tupli.repository.chat.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

/**
 * 시간 종료시 방 파괴 키가 expired -> Listener로 듣고 해당 방 파괴
 */
@Component
public class RedisKeyExpiredListener extends KeyExpirationEventMessageListener {

    private final ChatRoomRepository chatRoomRepository;

    /**
     * Creates new {@link MessageListener} for {@code __keyevent@*__:expired} messages.
     * @param listenerContainer must not be {@literal null}.
     * */
    public RedisKeyExpiredListener(@Qualifier("redisMessageListener")
                                           RedisMessageListenerContainer listenerContainer, ChatRoomRepository chatRoomRepository) {
        super(listenerContainer);
        this.chatRoomRepository = chatRoomRepository;
    }
    /** * * @param message redis key
     * * @param pattern __keyevent@*__:expired
     * */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        chatRoomRepository.deleteChatRoom(message.toString());
//        String key = new String(message.getBody());
//        System.out.println(new String(message.getChannel()));
//        System.out.println(key);
//        System.out.println("########## onMessage pattern " +
//                new String(pattern) + " | " + message.toString());

    }
}

