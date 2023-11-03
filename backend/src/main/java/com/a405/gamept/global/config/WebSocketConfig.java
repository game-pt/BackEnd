package com.a405.gamept.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final String prodUrl, localUrl, localPortUrl;

    public WebSocketConfig(@Value("${url.prod}") String prodUrl, @Value("${url.local}") String localUrl, @Value("${url.local-port}") String localPortUrl) {
        this.prodUrl = prodUrl;
        this.localUrl = localUrl;
        this.localPortUrl = localPortUrl;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue");  // 메시지 브로커 등록
    }
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint("/ws")
                // 특정 원격 출처에서의 접속 허용
                .setAllowedOriginPatterns(prodUrl, localUrl, localPortUrl)
                .withSockJS();  // SockJS 지원을 통해 WebSocket 연결 설정
    }
}
