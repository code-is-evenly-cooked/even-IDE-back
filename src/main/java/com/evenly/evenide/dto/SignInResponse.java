package com.evenly.evenide.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
@Getter
@AllArgsConstructor
public class SignInResponse {
    private String accessToken;
    private String refreshToken;
    private Long userId;
    private String nickname;
    private String provider;
}
