package com.a405.gamept.game.dto.command;

import lombok.Builder;

/**
 * 사용자 또는 몬스터가 죽었는지 파악하기 위해 내부 로직에서 사용하는 record
 * @author : 지환
 */

@Builder
public record AttackedCommandDto(
        String prompt,
        int playerHp,
        String endYn
) {
        public AttackedCommandDto of(String prompt, int playerHp, String endYn){
                return AttackedCommandDto.builder()
                        .prompt(prompt)
                        .playerHp(playerHp)
                        .endYn(endYn)
                        .build();
        }
}
