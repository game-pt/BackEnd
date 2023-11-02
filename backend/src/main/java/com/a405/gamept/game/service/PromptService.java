package com.a405.gamept.game.service;

import com.a405.gamept.game.dto.command.GetPromptResultCommandDto;
import com.a405.gamept.game.dto.response.GetPromptResultResponseDto;

public interface PromptService {

    GetPromptResultResponseDto getPrmoptResult(GetPromptResultCommandDto getPromptResultCommandDto);

}
