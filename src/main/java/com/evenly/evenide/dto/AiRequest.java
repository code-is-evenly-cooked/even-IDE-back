package com.evenly.evenide.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AiRequest {
    private String mode;
    private String prompt;
    private String code;
}
