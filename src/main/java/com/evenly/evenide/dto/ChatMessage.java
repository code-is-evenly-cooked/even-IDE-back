package com.evenly.evenide.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

    public enum MessageType {
        JOIN, MESSAGE
    }

    private MessageType type;
    private String projectId;
    private String sender; //내부 식별용
    private String nickname;
    private String content;
}
