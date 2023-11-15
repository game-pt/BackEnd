package com.a405.gamept.util.dto.request;

public record ChatGptRequestDto(
        String model,
        ChatGptMessage[] messages,
        int temperature,
        int max_tokens
) {
}
