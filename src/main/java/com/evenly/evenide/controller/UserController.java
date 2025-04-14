package com.evenly.evenide.controller;

import com.evenly.evenide.Config.Security.JwtUtil;
import com.evenly.evenide.dto.JwtUserInfoDto;
import com.evenly.evenide.dto.PasswordUpdateDto;
import com.evenly.evenide.dto.UserResponseDto;
import com.evenly.evenide.entity.User;
import com.evenly.evenide.global.response.MessageResponse;
import com.evenly.evenide.repository.UserRepository;
import com.evenly.evenide.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;
    private final JwtUtil jwtUtil;

    // 마이페이지
    @GetMapping
    public ResponseEntity<UserResponseDto> getUserInfo(@AuthenticationPrincipal JwtUserInfoDto userInfo) {
        User user = authService.getUserInfo(userInfo.getUserId());
        UserResponseDto responseDto = new UserResponseDto(user.getEmail(), user.getNickname());
        return ResponseEntity.ok(responseDto);
    }

    // 비밀번호 변경
    @PatchMapping("/update-password")
    public ResponseEntity<MessageResponse> updatePassword(@RequestHeader("Authorization") String token,
                                                         @RequestBody @Valid PasswordUpdateDto dto){
        // 유저 ID 추출
        String resolveToken = jwtUtil.resolveToken(token);
        String userId = jwtUtil.getUserIdFromToken(resolveToken);

        // 비밀번호 업데이트
        authService.updatePassword(userId, dto.getCurrentPassword(), dto.getNewPassword());

        // 기존 accessToken 블랙리스트 처리
        jwtUtil.blacklistToken(resolveToken);

        return ResponseEntity.ok(new MessageResponse("success"));
    }

}
