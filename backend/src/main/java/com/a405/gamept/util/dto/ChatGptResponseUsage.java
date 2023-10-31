package com.a405.gamept.util.dto;

public record ChatGptResponseUsage(
        int prompt_tokens,
        int completion_tokens,
        int total_tokens
) {
}
