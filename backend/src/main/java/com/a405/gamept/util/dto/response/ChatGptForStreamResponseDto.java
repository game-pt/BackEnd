package com.a405.gamept.util.dto.response;

public record ChatGptForStreamResponseDto(
        String id,
        String object,
        int created,
        String model,
        String system_fingerprint,
        ChatGptChoiceForStream[] choices
) {
}