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
        @NotBlank /*@Size(7)*/ String code,
        @NotBlank @Size(min=2, max=10) String name,
        @Valid @Min(1) List<RaceStatGetResponseDto> statList
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
