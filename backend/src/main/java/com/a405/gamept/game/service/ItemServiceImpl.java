package com.a405.gamept.game.service;

import com.a405.gamept.game.dto.command.ItemGetCommandDto;
import com.a405.gamept.game.dto.command.ItemSetCommandDto;
import com.a405.gamept.game.dto.response.ItemGetResponseDto;
import com.a405.gamept.game.dto.response.ItemSetResponseDto;
import com.a405.gamept.game.entity.Item;
import com.a405.gamept.game.repository.ItemRepository;
import com.a405.gamept.game.util.FinalData;
import com.a405.gamept.game.util.enums.GameErrorMessage;
import com.a405.gamept.game.util.exception.GameException;
import com.a405.gamept.play.entity.Game;
import com.a405.gamept.play.entity.Player;
import com.a405.gamept.play.repository.GameRedisRepository;
import com.a405.gamept.play.repository.PlayerRedisRepository;
import com.a405.gamept.util.ValidateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final GameRedisRepository gameRepository;
    private final PlayerRedisRepository playerRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public ItemSetResponseDto setItem(ItemSetCommandDto itemSetCommandDto) throws GameException {
        ValidateUtil.validate(itemSetCommandDto);

        Game game = gameRepository.findById(itemSetCommandDto.gameCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.GAME_NOT_FOUND));

        Player player = playerRepository.findById(itemSetCommandDto.playerCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.PLAYER_NOT_FOUND));

        boolean flag = false;  // 방에 존재하는 사용자인지 체크하는 로직
        for (String playerCode : game.getPlayerList()) {
            if(playerCode.equals(player.getCode())) {
                flag = true;
                break;
            }
        }
        if(!flag) {  // 플레이어가 방에 존재하지 않을 경우
            throw new GameException(GameErrorMessage.PLAYER_NOT_FOUND);
        }

        // 얻을 아이템 추출
        Item item = itemRepository.findById(player.getNewItemCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.ITEM_INVALID));

        // 아이템 삽입
        List<String> itemCodeList = player.getItemCodeList();
        if(itemCodeList == null) {  // 아이템 존재하지 않을 경우 새로 생성
            itemCodeList = new ArrayList<>();
        } else if (FinalData.MAX_ITEM_SIZE <= itemCodeList.size()) {  // 아이템 소지 수가 최대값을 넘을 경우
            throw new GameException(GameErrorMessage.ITEM_FULL);
        }
        itemCodeList.add(item.getCode());

        // 삽입된 아이템 저장
        playerRepository.save(player.toBuilder().newItemCode("").itemCodeList(itemCodeList).build());

        List<ItemGetResponseDto> itemGetResponseDtoList = new ArrayList<>();

        ItemGetResponseDto itemGetResponseDto;
        for(String itemCode : itemCodeList) {
            itemGetResponseDto = ItemGetResponseDto.from(itemRepository.findById(itemCode)
                    .orElseThrow(() -> new GameException(GameErrorMessage.ITEM_INVALID)));
            ValidateUtil.validate(itemGetResponseDto);

            // 유효성 검사 후 삽입
            itemGetResponseDtoList.add(itemGetResponseDto);
        }

        ItemSetResponseDto itemSetResponseDto = ItemSetResponseDto.builder()
                .itemCode(item.getCode())
                .playerCode(player.getCode())
                .itemList(itemGetResponseDtoList)
                .build();

        ValidateUtil.validate(itemSetResponseDto);
        return itemSetResponseDto;
    }
}
