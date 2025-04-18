package com.evenly.evenide.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.swing.text.Position;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CodeDiffMessage {
    private String type;
    private Long projectId;
    private Long fileId;
    private String sender;
    private String nickname;
    private String text;
    private Position from;
    private Position to;
    private LocalDateTime timestamp;

    @Getter
    @Setter
    public static class Position {
        private int lineNumber;
        private int column;
    }
}
