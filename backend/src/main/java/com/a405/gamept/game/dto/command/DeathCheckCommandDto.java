package com.a405.gamept.game.dto.command;

import com.a405.gamept.game.entity.Subtask;
import com.a405.gamept.play.entity.Player;
import jakarta.validation.constraints.NotBlank;
import lombok.NoArgsConstructor;

/**
 * 사용자 또는 몬스터가 죽었는지 파악하기 위해 내부 로직에서 사용하는 record
 * @param prompt : 현재 전투 상황
 * @param endYn : 전투 종료 여부
 * @author : 지환
 */

public record DeathCheckCommandDto(
        @NotBlank(message = "전투 상황은 필수입니다.")
        String prompt,
        @NotBlank(message = "전투 종료 여부는 필수입니다.")
        String endYn,
        int playerHp,
        Player player
) {
        public DeathCheckCommandDto of(String prompt, String endYn, int playerHp, Player player){
                return new DeathCheckCommandDto(prompt, endYn, playerHp, player);
        }

        public DeathCheckCommandDto from(DeathCheckCommandDto deathCheckCommandDto){
                return new DeathCheckCommandDto(deathCheckCommandDto.prompt, deathCheckCommandDto.endYn, deathCheckCommandDto.playerHp, deathCheckCommandDto.player);
        }
}
