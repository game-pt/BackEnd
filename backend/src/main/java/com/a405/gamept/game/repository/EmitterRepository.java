package com.a405.gamept.game.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@RequiredArgsConstructor
public class EmitterRepository {
    // 모든 Emitters를 저장하는 ConcourrentHashMap
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    /**
     * 주어진 아이디와 이미터를 저장
     *
     * @param gameCode  - 게임 코드
     * @param emitter   - 이벤트 Emitter
     */
    public void save(String gameCode, SseEmitter emitter) {
        emitters.put(gameCode, emitter);
    }

    /**
     * 주어진 아이디의 Emitter를 제거
     * 
     * @param gameCode  - 게임 코드
     */
    public void deleteById(String gameCode) {
        emitters.remove(gameCode);
    }

    /**
     * 주어진 아이디의 Emitter를 가져옴
     *
     * @param gameCode  - 게임 코드
     * @return
     */
    public SseEmitter get(String gameCode) {
        return emitters.get(gameCode);
    }

}
