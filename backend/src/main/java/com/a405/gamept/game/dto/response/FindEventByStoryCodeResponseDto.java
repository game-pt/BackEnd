package com.a405.gamept.game.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class FindEventByStoryCodeResponseDto {

    /**
     * code: 이벤트의 고유 코드
     */
    private String code;

    /**
     * name: 이벤트의 이름
     */
    private String name;

    /**
     * prompt: ChatGPT 명령어
     */
    private String prompt;

    /**
     * item_yn: 해당 이벤트로부터 아이템을 획득 가능한가의 여부
     */
    private char item_yn;

    /**
     * weight: 이벤트가 랜덤으로 발생할 확률에 대한 가중치
     */
    private int weight;

    @Builder
    public FindEventByStoryCodeResponseDto(String code, String name, String prompt, char item_yn, int weight) {
        this.code = code;
        this.name = name;
        this.prompt = prompt;
        this.item_yn = item_yn;
        this.weight = weight;
    }

}
