package com.a405.gamept.member.dto.command;

import com.a405.gamept.member.dto.response.KakaoTokenResopnseDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record KakaoUserCommandDto(
        @NotBlank(message = "액세스 토큰이 존재하지 않습니다.")
        String accessToken,
        @NotBlank(message = "리프레시 토큰이 존재하지 않습니다.")
        String refreshToken,
        @Positive(message = "액세스 토큰 만료 시간은 양수여야 합니다.")
        int expiresIn,
        @Positive(message = "리프레시 토큰 만료 시간은 양수여야 합니다.")
        int refreshTokenExpiresIn
) {
    public static KakaoUserCommandDto from(KakaoTokenResopnseDto kakaoTokenResopnseDto) {
        return KakaoUserCommandDto.builder()
                .accessToken(kakaoTokenResopnseDto.accessToken())
                .refreshToken(kakaoTokenResopnseDto.refreshToken())
                .expiresIn(kakaoTokenResopnseDto.expiresIn())
                .refreshTokenExpiresIn(kakaoTokenResopnseDto.refreshTokenExpiresIn())
                .build();
    }
}
