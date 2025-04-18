package com.evenly.evenide.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CodeCursorMessage {
    private String type;
    private Long projectId;
    private Long fileId;
    private String sender;
    private String nickname;
    private CursorPosition cursor;
    private LocalDateTime timestamp;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CursorPosition {
        private int line;
    }
}
