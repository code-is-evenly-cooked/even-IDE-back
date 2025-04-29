package com.evenly.evenide.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ChatEnterRequestDto {

    @NotNull(message = "projectId는 필수입니다.")
    Long projectId;

//    @NotNull(message = "fileId는 필수입니다.")
    Long fileId;
}
