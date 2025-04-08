package com.evenly.evenide.controller;

import com.evenly.evenide.dto.SignUpDto;
import com.evenly.evenide.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
