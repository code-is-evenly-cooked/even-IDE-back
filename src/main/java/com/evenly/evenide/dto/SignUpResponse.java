package com.evenly.evenide.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class SignUpResponse {
    private Long userId;
    private String email;
    private String nickname;

    public SignUpResponse(Long userId, String email, String nickname) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
    }
}
