package com.a405.gamept.play.entity;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "prompt")
@Getter
@Builder
public class Prompt {

    /**
     * [pk] code : prompt 구분 값
     */
    @Id
    private String code;

    /**
     * memo : prompt 내용
     */
    private String memo;
}
