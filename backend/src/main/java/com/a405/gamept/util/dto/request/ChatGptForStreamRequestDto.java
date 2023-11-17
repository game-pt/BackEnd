package com.a405.gamept.util.dto.request;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record ChatGptForStreamRequestDto(
        String model,
        ChatGptMessage[] messages,
        int temperature,
        int max_tokens,
        boolean stream
) {
    public static ChatGptForStreamRequestDto of(
            String model,
            ChatGptMessage[] messages,
            int temperature,
            int max_tokens,
            boolean stream
    ) {
        return ChatGptForStreamRequestDto.builder()
                .model(model)
                .messages(messages)
                .temperature(temperature)
                .max_tokens(max_tokens)
                .stream(stream)
                .build();
    }
}
