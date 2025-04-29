package com.evenly.evenide.service;

import com.evenly.evenide.dto.CodeDiffMessage;
import com.evenly.evenide.dto.CodeUpdateMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CodeSyncService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private static final int MAX_LOG_COUNT = 1000;
    private static final int MAX_DIFF_LOG = 2000;

    public void saveCodeUpdateLog(CodeUpdateMessage message) {
        String key = "code:" + message.getProjectId() + ":" + message.getFileId();

        try {
            String json = objectMapper.writeValueAsString(message);
            redisTemplate.opsForList().rightPush(key, json);

            Long size = redisTemplate.opsForList().size(key);
            if (size != null && size > MAX_LOG_COUNT) {
                redisTemplate.opsForList().trim(key, size - MAX_LOG_COUNT, -1);
            }
        } catch (JsonProcessingException e) {
            log.error("코드 수정 로그 Redis 저장 실패", e);
        }
    }

    public void saveDiffToRedis(CodeDiffMessage message) {
        String key = "diff:" + message.getProjectId() + ":" + message.getFileId();

        if (message.getTimestamp() == null) {
            message.setTimestamp(LocalDateTime.now());
        }

        try {
            String json = objectMapper.writeValueAsString(message);
            redisTemplate.opsForList().rightPush(key, json);
            redisTemplate.opsForList().trim(key, - MAX_DIFF_LOG, -1);
        } catch (JsonProcessingException e) {
            log.error("코드 diff 수정 로그 Redis 저장 실패", e);
        }
    }
}
