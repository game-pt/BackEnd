package com.a405.gamept.game.controller;

import com.a405.gamept.game.dto.command.ItemDeleteCommandDto;
import com.a405.gamept.game.dto.command.ItemSetCommandDto;
import com.a405.gamept.game.dto.request.ItemDeleteRequestDto;
import com.a405.gamept.game.dto.request.ItemSetRequestDto;
import com.a405.gamept.game.dto.response.ItemDeleteResponseDto;
import com.a405.gamept.game.dto.response.ItemSetResponseDto;
import com.a405.gamept.game.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@RestController
@RequestMapping("item")
public class ItemController {
    private final SimpMessagingTemplate webSocket;
    private final ItemService itemService;

    @Autowired
    public ItemController(SimpMessagingTemplate webSocket, ItemService itemService) {
        this.webSocket = webSocket;
        this.itemService = itemService;
    }

    @MessageMapping("/item")
    public void setItem(@Payload @Valid ItemSetRequestDto itemSetRequestDto) {
        ItemSetResponseDto itemSetResponseDto = itemService.setItem(ItemSetCommandDto.from(itemSetRequestDto));
        webSocket.convertAndSendToUser(itemSetResponseDto.playerCode(), "/item", itemSetResponseDto.itemList());
    }

    @MessageMapping("/item/{itemCode}")
    public void deleteItem(@DestinationVariable String itemCode, @Payload @Valid ItemDeleteRequestDto itemDeleteRequestDto) {
        ItemDeleteResponseDto itemDeleteResponseDto = itemService.deleteItem(ItemDeleteCommandDto.from(itemDeleteRequestDto, itemCode));
        webSocket.convertAndSendToUser(itemDeleteResponseDto.playerCode(), "/item", itemDeleteResponseDto.itemList());
    }

}
