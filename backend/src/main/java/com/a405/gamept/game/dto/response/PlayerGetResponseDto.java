package com.a405.gamept.game.dto.response;

import com.a405.gamept.game.util.GameData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.util.List;

@Builder
public record PlayerGetResponseDto (
        @NotBlank(message = "플레이어 닉네임이 존재하지 않습니다.")
        @Size(min = 1, max = 20, message = "닉네임은 1글자 이상, 20글자 이하여야 합니다.")
        String nickname,

        @NotNull(message = "플레이어 종족이 존재하지 않습니다.")
        @Valid
        PlayerRaceGetResponseDto race,

        @Valid
        @NotNull(message = "플레이어 직업이 존재하지 않습니다.")
        PlayerJobGetResponseDto job,

        @Positive(message = "플레이어의 체력은 양수여야 합니다.")
        @Max(value = GameData.MAX_STAT * 10, message = "플레이어의 체력은 " + GameData.MAX_STAT * 10 + "을 넘을 수 없습니다.")
        int hp,

        @PositiveOrZero(message = "플레이어의 경험치는 음수가 될 수 없습니다.")
        int exp,

        @Positive(message = "플레이어의 레벨은 양수여야 합니다.")
        int level,

        @NotNull(message = "플레이어의 스탯이 존재하지 않습니다.")
        List<StatGetResponseDto> statList,

        @Size(max = GameData.MAX_ITEM_SIZE, message = "플레이어의 아이템은 " + GameData.MAX_ITEM_SIZE + "개를 넘을 수 없습니다.")
        List<ItemGetResponseDto> itemList

) { }
