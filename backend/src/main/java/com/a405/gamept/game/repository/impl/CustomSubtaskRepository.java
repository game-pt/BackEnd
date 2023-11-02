package com.a405.gamept.game.repository.impl;

import com.a405.gamept.game.dto.response.SubtaskResponseDto;
import com.a405.gamept.game.entity.Subtask;
import com.a405.gamept.game.repository.SubtaskRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomSubtaskRepository implements SubtaskRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<SubtaskResponseDto> getCodeAndName(Subtask subtask) {
        return null;
    }
}
