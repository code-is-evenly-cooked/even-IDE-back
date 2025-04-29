package com.evenly.evenide.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FileRequestDto {
    @NotBlank(message = "파일 이름은 필수입니다.")
    private String fileName;
}
