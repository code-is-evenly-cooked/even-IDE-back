package com.evenly.evenide.controller;

import com.evenly.evenide.config.security.JwtUtil;
import com.evenly.evenide.dto.*;
import com.evenly.evenide.entity.User;
import com.evenly.evenide.global.response.MessageResponse;
import com.evenly.evenide.repository.RefreshTokenRepository;
import com.evenly.evenide.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> signup(@RequestBody @Valid SignUpDto signUpDto){
        User user = authService.signup(signUpDto);
        SignUpResponse response = new SignUpResponse(user.getId(), user.getEmail(), user.getNickname());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/check-email")
    public ResponseEntity<MessageResponse> checkEmail(@RequestParam String email){
        authService.checkEmail(email);
        return ResponseEntity.ok(new MessageResponse("사용 가능한 이메일입니다."));
    }

    @GetMapping("/check-nickname")
    public ResponseEntity<MessageResponse> checkNickname(@RequestParam String nickname){
        authService.checkNickname(nickname);
        return ResponseEntity.ok(new MessageResponse("사용 가능한 닉네임입니다."));
    }

    @PostMapping("/login")
    public ResponseEntity<SignInResponse> login(@RequestBody @Valid SignInDto signInDto){
        return ResponseEntity.ok(authService.login(signInDto));

    }

    @PostMapping("/refresh")
    public ResponseEntity<SignInResponse> refreshToken(@RequestHeader("Authorization")String token){
        String refreshToken = jwtUtil.resolveToken(token);
        SignInResponse newTokens = authService.refresh(refreshToken);
        return ResponseEntity.ok(newTokens);
    }

    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logout(HttpServletRequest request){
        String accessToken = jwtUtil.resolveToken(request);
        String userId = jwtUtil.getUserIdFromToken(accessToken);

        jwtUtil.blacklistToken(accessToken);
        refreshTokenRepository.deleteById(Long.valueOf(userId));
        return ResponseEntity.ok(new MessageResponse("success"));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<MessageResponse> forgotPassword(@RequestBody @Valid EmailRequestDto userInfoDto){
        authService.sendResetEmail(userInfoDto.getEmail());
        return ResponseEntity.ok(new MessageResponse("success"));
    }

    @PostMapping("/password-reset")
    public ResponseEntity<MessageResponse> resetPassword(@RequestBody @Valid PasswordResetRequestDto requestDto){
        authService.resetPassword(requestDto.getToken(), requestDto.getNewPassword());
        return ResponseEntity.ok(new MessageResponse("success"));
    }

}
