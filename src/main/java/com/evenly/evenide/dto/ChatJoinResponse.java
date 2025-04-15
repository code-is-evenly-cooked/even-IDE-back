package com.evenly.evenide.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatJoinResponse {
    Long projectId;
    String sender;
    String nickname;
    String subscribeTopic;
    String sendJoinPath;
    String sendMessagePath;
}
