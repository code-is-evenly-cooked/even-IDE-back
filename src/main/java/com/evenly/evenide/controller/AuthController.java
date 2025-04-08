package com.evenly.evenide.controller;

import com.evenly.evenide.dto.SignUpDto;
import com.evenly.evenide.service.AuthService;
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

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody @Valid SignUpDto signUpDto){
        authService.signup(signUpDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공!");
    }

    @GetMapping("/check-email")
    public ResponseEntity<String> checkEmail(@RequestParam String email){
        authService.checkEmail(email);
        return ResponseEntity.ok("사용 가능한 이메일입니다.");
    }

    @GetMapping("/check-nickname")
    public ResponseEntity<String> checkNickname(@RequestParam String nickname){
        authService.checkNickname(nickname);
        return ResponseEntity.ok("사용 가능한 닉네임입니다.");
    }
}
