package com.evenly.evenide.mock;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/mock/auth")
public class MockAuthController {

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> req) {
        return ResponseEntity.ok(Map.of(
                "userId", 1,
                "email", req.get("email"),
                "nickname", req.get("nickname")
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> req) {
        return ResponseEntity.ok(Map.of(
                "accessToken", "jwt.token.string",
                "refreshToken", "jwt.token.string"
        ));
    }

    @PostMapping("/password-reset")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> req) {
        return ResponseEntity.ok(Map.of("message", "비밀번호 재설정 메일 발송됨"));
    }

    @PostMapping("/social")
    public ResponseEntity<?> socialLogin(@RequestBody Map<String, String> req) {
        return ResponseEntity.ok(Map.of(
                "accessToken", "jwt.token.string",
                "refreshToken", "jwt.token.string"
        ));
    }
}
