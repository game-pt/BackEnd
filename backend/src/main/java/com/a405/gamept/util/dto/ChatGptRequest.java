package com.a405.gamept.util.dto;

import java.util.List;

public record ChatGptRequest(
        String model,
        ChatGptRequestMessage[] messages,
        int temperature,
        int max_tokens
) {
}
