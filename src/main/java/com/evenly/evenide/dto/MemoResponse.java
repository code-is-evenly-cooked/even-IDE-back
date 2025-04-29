package com.evenly.evenide.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemoResponse {
    private Long memoId;
    private String memo;
    private Long writerId;
    private String writerNickName;
}
