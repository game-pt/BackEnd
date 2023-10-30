package com.a405.gamept.game.dto.response;

import com.a405.gamept.game.entity.Race;
import com.a405.gamept.game.entity.RaceStat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Builder(access = AccessLevel.PRIVATE)
@Slf4j
public record RaceGetResponseDto(
        @NotBlank(message = "종족 코드가 올바르지 않습니다.") /*@Size(7)*/ String code,
        @NotBlank(message = "종족 이름이 올바르지 않습니다.") @Size(min = 2, max = 10, message = "종족의 이름은 2글자 이상, 10글자 이하여야 합니다.") String name,
        @Valid @Size(min = 1, message = "스탯이 최소 한 개 이상 존재해야 합니다.") List<RaceStatGetResponseDto> statList
) {
    public static RaceGetResponseDto from(Race race) {
        List<RaceStat> statList = race.getRaceStatList();
        List<RaceStatGetResponseDto> statGetResponseDtoList = new ArrayList<>();

        for (RaceStat stat : statList) {
            statGetResponseDtoList.add(RaceStatGetResponseDto.from(stat));
        }

        return RaceGetResponseDto.builder()
                .code(race.getCode())
                .name(race.getName())
                .statList(statGetResponseDtoList)
                .build();
    }
}
