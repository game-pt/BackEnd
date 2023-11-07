package com.a405.gamept.play.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "prompt")
@Getter
@Builder
public class Prompt {

    /**
     * [pk] gameCode : prompt 구분 값
     */
    @Id
    private String code;

    /**
     * role: prompt를 보낸 주체 (system, user, assistant)
     */
    private String role;

    /**
     * content : prompt 내용
     */
    private String content;
}
