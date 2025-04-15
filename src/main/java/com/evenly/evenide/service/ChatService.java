package com.evenly.evenide.service;

import com.evenly.evenide.dto.ChatMessage;
import com.evenly.evenide.global.util.RandomNameGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final SimpMessageSendingOperations messageSendingOperations;

    public void sendMessage(ChatMessage message) {
        String destination = "/topic/project/" + message.getProjectId();
        messageSendingOperations.convertAndSend(destination,message);
    }

    public void join(ChatMessage message) {
        if (message.getSender() == null || message.getNickname() == null) {
            message.setSender(RandomNameGenerator.generateSenderId());
            message.setNickname(RandomNameGenerator.generateNickname());
        }

        message.setContent(message.getNickname() + "님이 입장했습니다.");
        String destination = "/topic/project/" + message.getProjectId();
        messageSendingOperations.convertAndSend(destination, message);
    }
}
