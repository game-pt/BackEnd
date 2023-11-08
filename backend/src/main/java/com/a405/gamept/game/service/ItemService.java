package com.a405.gamept.game.service;

import com.a405.gamept.game.dto.command.ItemSetCommandDto;
import com.a405.gamept.game.dto.response.ItemSetResponseDto;
import com.a405.gamept.game.util.exception.GameException;

public interface ItemService {
    /**
     * 아이템을 업데이트 한다.
     * @param itemSetCommandDto : 게임코드, 플레이어 코드
     * @return : 아이템을 해당 플레이어에게 응답
     * @author : 유영
     */
    ItemSetResponseDto setItem(ItemSetCommandDto itemSetCommandDto) throws GameException;
}
