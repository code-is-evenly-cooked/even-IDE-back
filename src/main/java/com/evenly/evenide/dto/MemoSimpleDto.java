package com.evenly.evenide.dto;

import java.time.LocalDateTime;

public record MemoSimpleDto (
    Long memoId,
    String content,
    LocalDateTime createdAt
    ){}
