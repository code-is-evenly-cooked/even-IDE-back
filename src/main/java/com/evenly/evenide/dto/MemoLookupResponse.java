package com.evenly.evenide.dto;

import java.util.List;

public record MemoLookupResponse (
    int lineNumber,
    List<MemoSimpleDto> memos
){}
