package com.evenly.evenide.global.util;

import com.evenly.evenide.global.exception.CustomException;
import com.evenly.evenide.global.exception.ErrorCode;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.function.Function;

@Component
public class SocialNameGenerator {

    public String generate(String provider) {
        String prefix = switch (provider.toUpperCase()) {
            case "GOOGLE" -> "구그리_";
            case "KAKAO" -> "코코아_";
            default -> "그냥 사용자_";
        };

        int randomNum = new Random().nextInt(9000) + 1000;
        return prefix + randomNum;
    }

    public String generateUnique(String provider, Function<String, Boolean> isDuplicate) {
        String nickname;
        int tryCount = 0;

        do {
            nickname = generate(provider);
            tryCount++;
            if (tryCount > 5) {
                throw new CustomException(ErrorCode.NICKNAME_GENERATION_FAILED);
            }
        } while (isDuplicate.apply(nickname));

        return nickname;
    }
}
