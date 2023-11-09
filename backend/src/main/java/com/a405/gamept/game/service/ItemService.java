package com.a405.gamept.game.service;

import com.a405.gamept.game.dto.command.ItemDeleteCommandDto;
import com.a405.gamept.game.dto.command.ItemSetCommandDto;
import com.a405.gamept.game.dto.response.ItemDeleteResponseDto;
import com.a405.gamept.game.dto.response.ItemGetResponseDto;
import com.a405.gamept.game.dto.response.ItemSetResponseDto;
import com.a405.gamept.game.util.exception.GameException;

import java.util.List;

public interface ItemService {
    /**
     * 아이템을 업데이트 한다.
     * @param itemSetCommandDto : 게임코드, 플레이어 코드
     * @return : 아이템 리스트를 해당 플레이어에게 응답
     * @author : 유영
     */
    ItemSetResponseDto setItem(ItemSetCommandDto itemSetCommandDto) throws GameException;


    /**
     * 아이템을 삭제한다.
     * @param itemDeleteCommandDto : 게임코드, 플레이어 코드, 아이템 코드
     * @return : 아이템 리스트를 해당 플레이어에게 응답
     * @author : 유영
     */
    ItemDeleteResponseDto deleteItem(ItemDeleteCommandDto itemDeleteCommandDto);

    List<ItemGetResponseDto> getItemGetResponseDtoList(List<String> itemCodeList);
}
