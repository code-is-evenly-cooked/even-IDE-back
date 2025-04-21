package com.evenly.evenide.global.util;

import com.evenly.evenide.dto.ChatMessage;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class ChatMessageSanitizer {

    private static final int MAX_LENGTH = 500;

    private static final String[] FORBIDDEN_WORDS = {
            "시발", "씨발", "ㅅㅂ", "ㅂㅅ", "병신", "미친", "존나", "ㅈㄴ",
            "꺼져", "죽어", "멍청이", "개새끼", "좆", "섹스", "ㅅㄲ", "ㅗ", "ㅉ", "애미", "ㅂㅅ같은",

            "fuck", "shit", "bitch", "asshole", "bastard", "dick", "pussy", "slut", "nigger", "fucker", "motherfucker"
    };

    public ChatMessage sanitize(ChatMessage chatMessage) {

        String content = chatMessage.getContent();

        for (String badWord : FORBIDDEN_WORDS) {
            if (badWord.isBlank()) continue;
            content = content.replaceAll("(?i)" + Pattern.quote(badWord), "*");
        }

        if (content.length() > MAX_LENGTH) {
            content = content.substring(0, MAX_LENGTH);
        }

        content = StringEscapeUtils.escapeHtml4(content);

        chatMessage.setContent(content);
        return chatMessage;
    }
}
