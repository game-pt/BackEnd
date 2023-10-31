package com.a405.gamept.util.dto.response;

public record ChatGptUsage(
        int prompt_tokens,
        int completion_tokens,
        int total_tokens
) {
}
