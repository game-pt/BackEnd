package com.a405.gamept.util.dto;

public record ChatGptResponseChoice(
        ChatGptResponseChoiceMessage message,
        int index,
        Object logprobs,
        String finish_reason
) {
}
