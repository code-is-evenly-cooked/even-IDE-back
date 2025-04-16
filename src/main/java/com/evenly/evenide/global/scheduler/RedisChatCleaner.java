package com.evenly.evenide.global.scheduler;

import com.evenly.evenide.dto.RedisChatMessageDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisChatCleaner {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 1000 * 60 * 60) // 1시간마다 실행
//    @Scheduled(fixedDelay = 1000 * 60 * 2) // 테스트용 2분
    public void cleanOldMessages() {
        log.info("Redis 정리 스케줄러 시작!");
        Set<String> keys = redisTemplate.keys("chat:*");
        if (keys.isEmpty()) return;

        for (String key : keys) {
            List<String> messages = redisTemplate.opsForList().range(key, 0, -1);
            if (messages == null || messages.isEmpty()) continue;

            List<String> filtered = messages.stream()
                    .map(json -> {
                        try {
                            return objectMapper.readValue(json, RedisChatMessageDto.class);
                        } catch (JsonProcessingException e) {
                            log.error("역직렬화 실패", e);
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .filter(msg -> msg.getSender().startsWith("anon-"))
                    .filter(msg -> msg.getTimestamp().isAfter(LocalDateTime.now().minusHours(24)))
//                    .filter(msg -> msg.getTimestamp().isAfter(LocalDateTime.now().minusMinutes(3))) // 테스트용 3분
                    .map(msg -> {
                        try {
                            return objectMapper.writeValueAsString(msg);
                        } catch (JsonProcessingException e) {
                            log.error("직렬화 실패", e);
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .toList();

            redisTemplate.delete(key);
            if (!filtered.isEmpty()) {
                redisTemplate.opsForList().rightPushAll(key, filtered);
            }
        }
    }
}
