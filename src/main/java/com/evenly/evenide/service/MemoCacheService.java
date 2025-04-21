package com.evenly.evenide.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemoCacheService {

    private final RedisTemplate<String, String> redisTemplate;

    // redis에 저장
    public void saveSnapShotLine(String fileId, String codeSnapshot, int lineNumber) {
        String key = fileId + "::" + codeSnapshot;
        redisTemplate.opsForValue().set(key,String.valueOf(lineNumber));
    }

    // redis에서 줄 번호 가져오는 부분
    public Optional<Integer> getLineNumberBySnapshot(String fileId, String codeSnapshot) {
        String key = fileId + "::" + codeSnapshot;
        String result = redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(result).map(Integer::parseInt);
    }

}
