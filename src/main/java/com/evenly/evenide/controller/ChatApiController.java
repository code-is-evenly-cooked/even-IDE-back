package com.evenly.evenide.controller;

import com.evenly.evenide.config.security.JwtUtil;
import com.evenly.evenide.dto.*;
import com.evenly.evenide.entity.User;
import com.evenly.evenide.global.exception.CustomException;
import com.evenly.evenide.global.exception.ErrorCode;
import com.evenly.evenide.global.util.RandomNameGenerator;
import com.evenly.evenide.repository.UserRepository;
import com.evenly.evenide.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatApiController {

    private final ChatService chatService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @PostMapping("/join")
    public ResponseEntity<ChatJoinResponse> join(
            @RequestBody @Valid ChatEnterRequestDto requestDto,
            @AuthenticationPrincipal JwtUserInfoDto userInfoDto
    ) {
        Long userId = (userInfoDto == null ? null : Long.valueOf(userInfoDto.getUserId()));
        Long projectId = requestDto.getProjectId();
        Long fileId = requestDto.getFileId();

        String sender;
        String nickname;

        System.out.println("userId = " + userId + ", projectId = " + projectId);

        if (userId != null) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
            sender = user.getId().toString();
            nickname = user.getNickname();
        } else {
            sender = RandomNameGenerator.generateSenderId();
            nickname = RandomNameGenerator.generateNickname();
        }

        return ResponseEntity.ok(new ChatJoinResponse(
                projectId,
                sender,
                nickname,
                new ChatJoinResponse.ChatPath(
                        "/topic/project/" + projectId,
                        "/app/chat.join",
                        "/app/chat.send"
                ),
                new ChatJoinResponse.EditorPath(
                        fileId,
                        new ChatJoinResponse.EditorPath.DiffPath(
                                "/topic/project/" + projectId + "/file/" + fileId,
                                "/app/code.diff"
                        ),
                        new ChatJoinResponse.EditorPath.CursorPath(
                                "/topic/project/" + projectId + "/file/" + fileId + "/cursor",
                                "/app/code.cursor"
                        )
                )
        ));
    }

    @GetMapping("/history")
    public ResponseEntity<List<ChatMessage>> getChatHistory(
            @RequestParam String projectId,
            @RequestHeader(value = "Authorization", required = false) String token
    ) {
        return ResponseEntity.ok(chatService.getRedisMessages(projectId, jwtUtil.resolveToken(token)));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ChatMessage>> searchMessage(
            @RequestParam String projectId,
            @RequestParam String keyword,
            @AuthenticationPrincipal JwtUserInfoDto userInfoDto
    ) {
        return ResponseEntity.ok(chatService.searchMessages(projectId, keyword));
    }

    @GetMapping("/context")
    public ResponseEntity<List<ChatMessage>> getContext(
            @RequestParam String projectId,
            @RequestParam String timestamp,
            @AuthenticationPrincipal JwtUserInfoDto userInfoDto
    ) {
        return ResponseEntity.ok(chatService.getContext(projectId, timestamp));
    }

}
