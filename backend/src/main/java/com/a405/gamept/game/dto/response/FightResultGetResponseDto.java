package com.a405.gamept.game.dto.response;

import com.a405.gamept.game.dto.command.GameStateCommandDto;
import com.a405.gamept.game.dto.command.PlayerStateCommandDto;
import jakarta.validation.constraints.NotBlank;

/**
 * 전투 결과 반환
 * @param prompt : 전투 결과
 * @param endYn : 이야기 진행 여부
 * @author : 지환
 */
public record FightResultGetResponseDto(
        @NotBlank(message = "행동 결과는 필수 입니다.")
        String prompt,
        @NotBlank(message = "이야기 도중 여부는 필수 입니다.")
        String endYn,
        @NotBlank(message = "게임 도중 여부는 필수 입니다.")
        String gameOverYn
) {
        public static FightResultGetResponseDto of(GameStateCommandDto gameStateCommandDto, String gameOverYn){
                return new FightResultGetResponseDto(gameStateCommandDto.prompt(), gameStateCommandDto.endYn(), gameOverYn);
        }
}
