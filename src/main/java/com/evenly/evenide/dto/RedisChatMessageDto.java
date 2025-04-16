package com.evenly.evenide.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RedisChatMessageDto {
    private String type;
    private String projectId;
    private String sender;
    private String nickname;
    private String content;
    private LocalDateTime timestamp;
}
