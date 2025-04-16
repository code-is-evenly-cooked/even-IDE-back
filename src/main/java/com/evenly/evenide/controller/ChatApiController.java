package com.evenly.evenide.controller;

import com.evenly.evenide.config.security.JwtUtil;
import com.evenly.evenide.dto.ChatEnterRequestDto;
import com.evenly.evenide.dto.ChatJoinResponse;
import com.evenly.evenide.dto.JwtUserInfoDto;
import com.evenly.evenide.entity.User;
import com.evenly.evenide.global.exception.CustomException;
import com.evenly.evenide.global.exception.ErrorCode;
import com.evenly.evenide.global.util.RandomNameGenerator;
import com.evenly.evenide.repository.UserRepository;
import com.evenly.evenide.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatApiController {

    private final ChatService chatService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @PostMapping("/join")
    public ResponseEntity<ChatJoinResponse> join(
            @RequestBody ChatEnterRequestDto requestDto,
            @AuthenticationPrincipal JwtUserInfoDto userInfoDto
    ) {
        Long userId = (userInfoDto == null ? null : Long.valueOf(userInfoDto.getUserId()));
        Long projectId = requestDto.getProjectId();

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
                "/topic/project/" + projectId,
                "/app/chat.join",
                "/app/chat.send"
        ));
    }
}
