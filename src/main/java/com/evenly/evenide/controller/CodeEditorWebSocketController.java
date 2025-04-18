package com.evenly.evenide.controller;


import com.evenly.evenide.dto.CodeCursorMessage;
import com.evenly.evenide.dto.CodeDiffMessage;
import com.evenly.evenide.dto.CodeUpdateMessage;
import com.evenly.evenide.service.CodeSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CodeEditorWebSocketController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final CodeSyncService codeSyncService;

    @MessageMapping("/code.update")
    public void handleCodeUpdate(CodeUpdateMessage message) {
        if (message.getTimestamp() == null) {
            message.setTimestamp(LocalDateTime.now());
        }

        codeSyncService.saveCodeUpdateLog(message);

        messagingTemplate.convertAndSend(
                "/topic/project/" + message.getProjectId() + "/file/" + message.getFileId(),
                message
        );
    }

    @MessageMapping("/code.diff")
    public void handleCodeDiff(CodeDiffMessage message) {
        codeSyncService.saveDiffToRedis(message);

        String destination = "/topic/project/" + message.getProjectId() + "/file/" + message.getFileId();

        messagingTemplate.convertAndSend(destination, message);
    }

    @MessageMapping("/code.cursor")
    public void handleCursorMove(CodeCursorMessage message) {
        messagingTemplate.convertAndSend(
                "/topic/project/" + message.getProjectId() + "/file/" + message.getFileId() + "/cursor",
                message
        );
    }
}
