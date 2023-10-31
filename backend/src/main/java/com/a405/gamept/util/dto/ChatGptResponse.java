package com.a405.gamept.util.dto;

public record ChatGptResponse(
        String id,
        String object,
        int created,
        String model,
        ChatGptResponseChoice[] choices,
        ChatGptResponseUsage usage
) {
}