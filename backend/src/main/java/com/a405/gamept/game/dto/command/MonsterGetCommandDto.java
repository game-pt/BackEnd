package com.a405.gamept.game.dto.command;

import com.a405.gamept.game.util.FinalData;
import com.a405.gamept.game.util.exception.GameInvalidException;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.misc.NotNull;

@Builder(builderMethodName = "innerBuilder")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Slf4j
public class MonsterGetCommandDto {
    private String storyCode;
    private int level;

    public static MonsterGetCommandDtoBuilder builder() {
        return MonsterGetCommandDto.innerBuilder();
    }

    public MonsterGetCommandDtoBuilder storyCode(String storyCode) throws GameInvalidException {
        if(storyCode == null || storyCode.trim().isEmpty()) {
            log.error("GameInvalidException : { MonsterGetCommandDto 스토리 코드 삽입 실패 }");
            throw new GameInvalidException();
        }

        return innerBuilder().storyCode(storyCode.trim());
    }

    public MonsterGetCommandDtoBuilder level(int level) throws GameInvalidException {
        if(level <= 0 || FinalData.MONSTER_MAX_LEVEL < level) {
            log.error("GameInvalidException : { MonsterGetCommandDto 레벨 삽입 실패 }");
            throw new GameInvalidException();
        }

        return innerBuilder().level(level);
    }
}
