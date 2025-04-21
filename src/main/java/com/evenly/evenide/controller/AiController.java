package com.evenly.evenide.controller;

import com.evenly.evenide.dto.AiRequest;
import com.evenly.evenide.dto.AiResponse;
import com.evenly.evenide.dto.JwtUserInfoDto;
import com.evenly.evenide.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ai")
public class AiController {

    private final AiService aiService;

    @PostMapping("/ask")
    public ResponseEntity<AiResponse> ask(
            @RequestBody AiRequest aiRequest,
            @AuthenticationPrincipal JwtUserInfoDto userInfoDto
    ) {
        String result = aiService.ask(aiRequest);
        return ResponseEntity.ok(new AiResponse(result));
    }
}
