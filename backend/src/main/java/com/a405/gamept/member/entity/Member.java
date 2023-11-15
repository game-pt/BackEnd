package com.a405.gamept.member.entity;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash(value = "member")
@Builder
public class Member {
    @Id
    private String email;
    @Indexed
    private String refreshToken;
    @TimeToLive
    private int expiresIn;

}
