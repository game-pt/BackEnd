package com.a405.gamept.member.dto.command;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record KakaoTokenCommandDto(
        // 토큰 받기 요청에 필요한 인가 코드
        @NotBlank(message = "카카오 코드가 존재하지 않습니다.")
        String code
) {
    public static KakaoTokenCommandDto of(String code) {
        return KakaoTokenCommandDto.builder()
                .code(code)
                .build();
    }
}
