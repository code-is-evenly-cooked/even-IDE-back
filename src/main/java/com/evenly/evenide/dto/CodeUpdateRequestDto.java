package com.evenly.evenide.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CodeUpdateRequestDto {

    @NotBlank
    private String language;

    private String content;

}
