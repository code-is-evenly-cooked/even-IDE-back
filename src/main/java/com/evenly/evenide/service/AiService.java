package com.evenly.evenide.service;

import com.evenly.evenide.dto.AiRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AiService {

    private final ChatClient chatClient;

    public String ask(AiRequest request) {
        String mode = Optional.ofNullable(request.getMode()).orElse("");
        String prompt = request.getPrompt();
        String code = Optional.ofNullable(request.getCode()).orElse("");

        String finalPrompt = switch (mode) {
            case "code-based" -> prompt + "\n\n[코드]\n" + code;
            case "hint" -> "아래 질문에 대해 문제를 푸는 데 도움이 되는 '힌트'를 알려줘. " +
                    "정답 코드나 직접적인 풀이 코드는 절대 제공하지 마! " +
                    "대신 개념 설명, 풀이 아이디어, 비슷한 예제 코드 등은 자유롭게 제공해줘. " +
                    "질문: " + prompt;
            case "refactor" -> "다음 코드에 대해 리팩토링 부탁해. 왜 그렇게 수정해야 하는지 간단한 설명도 부탁할게!: \n\n" + code;
            default -> prompt;
        };

        return chatClient.prompt()
                .user(finalPrompt)
                .call()
                .content();
    }
}
