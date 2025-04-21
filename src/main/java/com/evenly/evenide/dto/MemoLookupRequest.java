package com.evenly.evenide.dto;

public record MemoLookupRequest(
        String fileId,
        String codeSnapshot
){}
