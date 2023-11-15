package com.a405.gamept.member.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record KakaoUserResopnseDto(
        String nickname,
        String email,
        @NotBlank(message = "액세스 토큰이 존재하지 않습니다.")
        String accessToken,
        @NotBlank(message = "리프레시 토큰이 존재하지 않습니다.")
        String refreshToken
) { }
