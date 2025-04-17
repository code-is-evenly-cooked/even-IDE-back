package com.evenly.evenide.service;

import com.evenly.evenide.config.security.JwtUtil;
import com.evenly.evenide.dto.ChatMessage;
import com.evenly.evenide.global.util.RandomNameGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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

    public List<ChatMessage> getRedisMessages(String projectId, String token) {
        boolean isAnon;
        try {
            isAnon = (token == null || !jwtUtil.validateAccessToken(token));
        } catch (Exception e) {
            isAnon = true;
        }
        String key = "chat:" + projectId;

        long now = Instant.now().toEpochMilli();

        long threshold = isAnon
                ? now - 1000L * 60 * 60 * 24
                : now - 1000L * 60 * 60 * 24 * 3;
        Set<String> jsonSet = redisTemplate.opsForZSet().rangeByScore(key, threshold, now);
        List<String> jsonList = new ArrayList<>(jsonSet);

        return jsonList.stream()
                .map(json -> {
                    try {
                        return objectMapper.readValue(json, ChatMessage.class);
                    } catch (JsonProcessingException e) {
                        log.error("Redis 메시지 역직렬화 실패", e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }
}
