package com.a405.gamept.util.dto.response;

public record ChatGptChoice(
        ChatGptChoiceMessage message,
        int index,
        Object logprobs,
        String finish_reason
) {
}
