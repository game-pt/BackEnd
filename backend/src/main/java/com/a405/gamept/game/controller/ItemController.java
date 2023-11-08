package com.a405.gamept.game.controller;

import com.a405.gamept.game.dto.command.ItemSetCommandDto;
import com.a405.gamept.game.dto.request.ItemSetRequestDto;
import com.a405.gamept.game.dto.response.ItemSetResponseDto;
import com.a405.gamept.game.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public void setItem(ItemSetRequestDto itemSetRequestDto) {
        ItemSetResponseDto itemSetResponseDto = itemService.setItem(ItemSetCommandDto.from(itemSetRequestDto));
        webSocket.convertAndSendToUser(itemSetResponseDto.playerCode(), "item", itemSetResponseDto.itemList());
    }

}
