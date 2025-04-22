package com.evenly.evenide.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemoUpdateRequest {
    private Long memoId;
    private String memo;
}
