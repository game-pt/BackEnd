package com.a405.gamept.play.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Builder(toBuilder = true)
public class Prompt {
    /**
     * role: prompt를 보낸 주체 (system, user, assistant)
     */
    private String role;

    /**
     * content : prompt 내용
     */
    private String content;
}
