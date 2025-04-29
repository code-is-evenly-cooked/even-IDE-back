package com.evenly.evenide.controller;

import com.evenly.evenide.dto.ChatMessage;
import com.evenly.evenide.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final ChatService chatService;

    @MessageMapping("/chat.send")
    public void sendMessage(ChatMessage message) {
        chatService.sendMessage(message);
    }

    @MessageMapping("/chat.join")
    public void join(ChatMessage message) {
        chatService.join(message);
    }
}
