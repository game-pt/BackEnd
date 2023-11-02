package com.a405.gamept.game.repository;

import com.a405.gamept.game.dto.response.SubtaskResponseDto;
import com.a405.gamept.game.entity.Subtask;
import java.util.List;

public interface SubtaskRepository {
    List<SubtaskResponseDto> getCodeAndName(Subtask subtask);
}
