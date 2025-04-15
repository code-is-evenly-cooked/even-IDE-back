package com.evenly.evenide.service;

import com.evenly.evenide.dto.CodeExecutionRequestDto;
import com.evenly.evenide.dto.CodeExecutionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class CodeRunService {

    private final RestTemplate restTemplate;

    public String runCode(String language, String content) {
        String url = "http://host.docker.internal:8081/code/execute";

        CodeExecutionRequestDto requestDto = new CodeExecutionRequestDto(language, content);

        ResponseEntity<CodeExecutionResponse> response = restTemplate.postForEntity(
                url,
                requestDto,
                CodeExecutionResponse.class
        );
        return response.getBody().getResult();
    }
}
