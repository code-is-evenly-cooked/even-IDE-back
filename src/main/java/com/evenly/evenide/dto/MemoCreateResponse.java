package com.evenly.evenide.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemoCreateResponse {
    private Long memoId;
    private String memo;
    private Long writerId;
    private String writerNickName;
}
