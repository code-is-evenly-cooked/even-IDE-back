package com.evenly.evenide.global.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisChatCleaner {

    private final RedisTemplate<String, String> redisTemplate;

    @EventListener(ApplicationReadyEvent.class)
    @Scheduled(fixedDelay = 1000L * 60 * 60 * 6) // 6시간마다 실행
    public void cleanOldMessages() {
        log.info("🧹 Redis 채팅 정리 스케줄러 시작");

        Set<String> keys = redisTemplate.keys("chat:*");
        if (keys.isEmpty()) return;

        long now = Instant.now().toEpochMilli();
        long expiredThreshold = now - 1000L * 60 * 60 * 24 * 4; // 4일 이전
        for (String key : keys) {
            Long removed = redisTemplate.opsForZSet().removeRangeByScore(key, 0, expiredThreshold);
            log.info("🧼 삭제된 메시지 수 (key: {}): {}", key, removed);
        }
    }
}
