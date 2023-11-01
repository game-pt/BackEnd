package com.a405.gamept.util.dto;

import java.util.List;

public record ChatGptRequestMessage(
        String role,
        String content
) {
}
