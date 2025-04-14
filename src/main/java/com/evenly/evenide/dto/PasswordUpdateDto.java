package com.evenly.evenide.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PasswordUpdateDto {
    private String currentPassword;
    private String newPassword;
}
