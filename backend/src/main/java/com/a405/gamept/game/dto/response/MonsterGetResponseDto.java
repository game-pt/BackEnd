package com.a405.gamept.game.dto.response;

import com.a405.gamept.game.util.exception.MonsterInvalidException;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Builder(builderMethodName = "innerBuilder")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Slf4j
public class MonsterGetResponseDto {
    private String name;
    private int level;

    public static MonsterGetResponseDtoBuilder builder() {
        return MonsterGetResponseDto.innerBuilder();
    }

    public MonsterGetResponseDtoBuilder name(String name) throws MonsterInvalidException {
        if(name == null || name.trim().isEmpty()) {
            log.error("MonsterInvalidException : { MonsterGetResponseDto 몬스터 이름 삽입 실패 }");
            throw new MonsterInvalidException();
        }

        return innerBuilder().name(name.trim());
    }

    public MonsterGetResponseDtoBuilder level(int level) throws MonsterInvalidException {
        if(level <= 0 || 10 < level) {
            log.error("GameInvalidException : { MonsterGetCommandDto 레벨 삽입 실패 }");
            throw new MonsterInvalidException();
        }

        return innerBuilder().level(level);
    }
}
