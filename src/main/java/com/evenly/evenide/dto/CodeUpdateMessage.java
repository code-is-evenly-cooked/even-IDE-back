package com.evenly.evenide.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class CodeUpdateMessage {
    private String type;
    private Long projectId;
    private Long fileId;
    private String sender;
    private String content;

    LocalDateTime timestamp;

    @Getter
    @Setter
    public static class Cursor {
        private int line;
        private int ch;
    }
}
