package com.evenly.evenide.service;

import com.evenly.evenide.dto.ChatMessage;
import com.evenly.evenide.dto.RedisChatMessageDto;
import com.evenly.evenide.global.util.RandomNameGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final SimpMessageSendingOperations messageSendingOperations;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;

    public void sendMessage(ChatMessage message) {
        saveToRedis(message);

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

    public void saveToRedis(ChatMessage message) {
        String key = "chat:" + message.getProjectId();

        if (message.getTimestamp() == null) {
            message.setTimestamp(LocalDateTime.now());
        }

        try {
            String json = objectMapper.writeValueAsString(message);
            long score = message.getTimestamp()
                            .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            redisTemplate.opsForZSet().add(key, json, score);
        } catch (JsonProcessingException e) {
            log.error("Redis 메시지 역직렬화 실패", e);
        }
    }

    public List<RedisChatMessageDto> getRedisMessages(String projectId) {
        String key = "chat:" + projectId;

        List<String> rawMessages = redisTemplate.opsForList().range(key, 0, -1);

        return rawMessages.stream()
                .map(json -> {
                    try {
                        return objectMapper.readValue(json, RedisChatMessageDto.class);
                    } catch (JsonProcessingException e) {
                        log.error("Redis 메시지 역직렬화 실패", e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }
}
