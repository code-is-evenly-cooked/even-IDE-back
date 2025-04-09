package com.evenly.evenide.controller;

import com.evenly.evenide.dto.SignUpDto;
import com.evenly.evenide.dto.SignUpResponse;
import com.evenly.evenide.entity.User;
import com.evenly.evenide.global.response.MessageResponse;
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
}
