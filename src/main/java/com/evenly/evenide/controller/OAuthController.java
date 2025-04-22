package com.evenly.evenide.controller;

import com.evenly.evenide.dto.OAuthTokenRequestDto;
import com.evenly.evenide.dto.SignInResponse;
import com.evenly.evenide.service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/oauth")
@RequiredArgsConstructor
public class OAuthController {

    private final OAuthService oAuthService;

    @PostMapping("/{provider}")
    public ResponseEntity<SignInResponse> oauthLogin(
            @PathVariable String provider,
            @RequestBody OAuthTokenRequestDto requestDto
    ) {
        SignInResponse response = oAuthService.loginWithOAuth(provider, requestDto.getAccessToken());
        return ResponseEntity.ok(response);
    }
}
