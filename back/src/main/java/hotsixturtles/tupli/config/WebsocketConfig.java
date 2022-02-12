package hotsixturtles.tupli.config;

import hotsixturtles.tupli.config.interceptor.StompHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@RequiredArgsConstructor
@Configuration
@EnableWebSocketMessageBroker  // STOMP 사용 선언
@CrossOrigin(origins="*")
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {

    private final StompHandler stompHandler;

    /**
     * pub/sub 메세징을 구현
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/sub");  // 메시지를 구독하는 요청의 prefix는 /sub로 시작
        config.setApplicationDestinationPrefixes("/pub");  // 메시지를 발행하는 요청의 prefix는 /pub로 시작
    }

    /**
     * stomp websocket의 연결 endpoint는 /ws-stomp로 설정
     * 예상 접속 주소 : ws://localhost:8080/ws-stomp
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-stomp")
                .setAllowedOriginPatterns("*")
//                .setAllowedOrigins("*")  // 3년전은 이거로 CORS 통과가 됐는데 지금은 안됨. 차후 체크
                .withSockJS();  // sock.js를 통하여 낮은 버전의 브라우저에서도 websocket이 동작할 수 있게
    }

    /**
     * StompHandler가 Websocket 앞단에서 token을 체크할 수 있도록 다음과 같이 인터셉터로 설정
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);
    }
}
