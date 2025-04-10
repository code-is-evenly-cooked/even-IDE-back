package com.evenly.evenide.controller;

import com.evenly.evenide.dto.SignInDto;
import com.evenly.evenide.dto.SignUpDto;
import com.evenly.evenide.dto.SignUpResponse;
import com.evenly.evenide.entity.User;
import com.evenly.evenide.global.response.MessageResponse;
import com.evenly.evenide.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

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
    public ResponseEntity<Map<String, String>> login(@RequestBody @Valid SignInDto signInDto, HttpServletResponse response){
        Map<String, String> tokens = authService.login(signInDto);
        String accessToken = tokens.get("access_token");
        String refreshToken = tokens.get("refresh_token");


        //accessToken 쿠키로 설정
        ResponseCookie accessCookie = ResponseCookie.from("accessToken", accessToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .maxAge(60*60)
                .build();

        response.setHeader("Set-Cookie", accessCookie.toString());
        return ResponseEntity.ok(tokens);

    }

}
