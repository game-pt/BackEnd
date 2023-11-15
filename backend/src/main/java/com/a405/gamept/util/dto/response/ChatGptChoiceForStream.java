package com.a405.gamept.util.dto.response;

public record ChatGptChoiceForStream(
        int index,
        ChatGptDelta delta,
        String finish_reason
) {
}
