package com.a405.gamept.game.service;

import com.a405.gamept.game.dto.command.PromptResultGetCommandDto;
import com.a405.gamept.game.dto.response.PromptResultGetResponseDto;

public interface PromptService {

    PromptResultGetResponseDto getPrmoptResult(PromptResultGetCommandDto promptResultGetCommandDto);

}
