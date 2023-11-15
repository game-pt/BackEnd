package com.a405.gamept.util.dto.response;

public record ChatGptResponseDto(
        String id,
        String object,
        int created,
        String model,
        ChatGptChoice[] choices,
        ChatGptUsage usage
) {
}