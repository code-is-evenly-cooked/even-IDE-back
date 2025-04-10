package com.evenly.evenide.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtUserInfoDto {
    private String userId;
    private String role;
}
