package com.evenly.evenide.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemoCreateRequest {
    private Long fileId;
    private String memo;
}
