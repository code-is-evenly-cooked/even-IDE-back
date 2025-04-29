package com.evenly.evenide.controller;

import com.evenly.evenide.dto.JwtUserInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

    @GetMapping("/secure")
    public ResponseEntity<String> testSecure(@AuthenticationPrincipal JwtUserInfoDto userInfo) {
        return ResponseEntity.ok("✅ 인증된 사용자! ID: " + userInfo.getUserId());
    }
}
