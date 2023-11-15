package com.a405.gamept.member.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record KakaoTokenResopnseDto(
        @NotBlank(message = "수집 정보가 존재하지 않습니다.")
        String scope,
        @NotBlank(message = "액세스 토큰이 존재하지 않습니다.")
        String accessToken,
        @NotBlank(message = "리프레시 토큰이 존재하지 않습니다.")
        String refreshToken,
        @Positive(message = "액세스 토큰 만료 시간은 양수여야 합니다.")
        int expiresIn,
        @Positive(message = "리프레시 토큰 만료 시간은 양수여야 합니다.")
        int refreshTokenExpiresIn
) { }
