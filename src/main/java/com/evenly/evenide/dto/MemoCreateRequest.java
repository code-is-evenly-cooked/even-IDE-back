package com.evenly.evenide.dto;

public record MemoCreateRequest (
    String fileId,
    String content
){}
