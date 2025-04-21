package com.evenly.evenide.service;

import com.evenly.evenide.config.security.JwtUtil;
import com.evenly.evenide.dto.ChatMessage;
import com.evenly.evenide.global.util.ChatMessageSanitizer;
import com.evenly.evenide.global.util.RandomNameGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final SimpMessageSendingOperations messageSendingOperations;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;
    private final ChatMessageSanitizer chatMessageSanitizer;

    public void sendMessage(ChatMessage originalMessage) {
        ChatMessage message = chatMessageSanitizer.sanitize(originalMessage);
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

    public List<ChatMessage> searchMessages(String projectId, String keyword) {
        String redisKey = "chat:" + projectId;

        long now = System.currentTimeMillis();
        long threeDaysAgo = now - Duration.ofDays(3).toMillis();

        Set<String> rawMessages = redisTemplate.opsForZSet().reverseRangeByScore(redisKey, threeDaysAgo, now); //최신순
        if (rawMessages == null) return List.of();

        return rawMessages.stream()
                .map(json -> {
                    try {
                        return objectMapper.readValue(json, ChatMessage.class);
                    } catch (JsonProcessingException e) {
                        log.error("Redis 메시지 역직렬화 실패", e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .filter(message -> message.getType() == ChatMessage.MessageType.MESSAGE)
                .filter(message -> message.getContent() != null && message.getContent().contains(keyword))
                .toList();
    }

    public List<ChatMessage> getContext(String projectId, String timestampStr) {
        String redisKey = "chat:" + projectId;

        long center = LocalDateTime.parse(timestampStr)
                .atZone(ZoneId.systemDefault())
                .toInstant().toEpochMilli();

        long now = System.currentTimeMillis();
        long threeDaysAgo = now - Duration.ofDays(3).toMillis(); // 3일을 넘지 않음.
        long margin = Duration.ofHours(24).toMillis(); // 검색한 메시지의 +-1일 메시지

        Set<ZSetOperations.TypedTuple<String>> messagesWithScore =
                redisTemplate.opsForZSet().rangeByScoreWithScores(
                        redisKey,
                        Math.max(threeDaysAgo, center - margin),
                        center + margin
                );
        if (messagesWithScore == null) return List.of();

        List<ChatMessage> parsed = messagesWithScore.stream()
                .sorted(Comparator.comparingDouble(tuple ->
                        tuple.getScore() != null ? tuple.getScore() : 0.0 // null 방지용 0.0
                ))
                .map(tuple -> {
                    try {
                        return objectMapper.readValue(tuple.getValue(), ChatMessage.class);
                    } catch (JsonProcessingException e) {
                        log.error("Redis 메시지 역직렬화 실패", e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .filter(message -> message.getType() == ChatMessage.MessageType.MESSAGE)
                .toList();

        int centerIndex = IntStream.range(0, parsed.size())
                .filter(i -> parsed.get(i).getTimestamp().equals(LocalDateTime.parse(timestampStr)))
                .findFirst()
                .orElse(-1);

        if (centerIndex == -1) return List.of();

        int from = Math.max(0, centerIndex - 10);
        int to = Math.min(parsed.size(), centerIndex + 11);

        return parsed.subList(from, to);
    }
}
