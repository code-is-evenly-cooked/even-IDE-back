package com.evenly.evenide.controller;

import com.evenly.evenide.dto.CodeExecutionRequestDto;
import com.evenly.evenide.dto.CodeExecutionResponse;
import com.evenly.evenide.service.CodeRunService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/code")
public class CodeRunController {

    private final CodeRunService codeRunService;

    @PostMapping("/execute")
    public ResponseEntity<CodeExecutionResponse> runCode(@RequestBody CodeExecutionRequestDto requestDto) {
        String result = codeRunService.runCode(requestDto.getLanguage(), requestDto.getContent());
        return ResponseEntity.ok(new CodeExecutionResponse(result));
    }
}
