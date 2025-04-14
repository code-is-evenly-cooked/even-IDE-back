package com.evenly.evenide.controller;

import com.evenly.evenide.dto.*;
import com.evenly.evenide.global.response.MessageResponse;
import com.evenly.evenide.service.FileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects/{projectId}/files")
public class FileController {

    private final FileService fileService;

    @PostMapping
    public ResponseEntity<EditorFileResponse> createFile(
            @PathVariable Long projectId,
            @RequestBody @Valid FileRequestDto requestDto,
            @AuthenticationPrincipal JwtUserInfoDto userInfo
    ) {
        Long userId = Long.valueOf(userInfo.getUserId());
        EditorFileResponse response = fileService.createFile(projectId, requestDto, userId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<EditorFileResponse> getFile(
            @PathVariable Long projectId,
            @PathVariable Long fileId,
            @AuthenticationPrincipal JwtUserInfoDto userInfoDto
    ) {
        Long userId = (userInfoDto == null ? null : Long.valueOf(userInfoDto.getUserId()));
        EditorFileResponse response = fileService.getFile(fileId, userId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{fileId}")
    public ResponseEntity<FileResponse> updateFileName(
            @PathVariable Long projectId,
            @PathVariable Long fileId,
            @RequestBody @Valid FileRequestDto requestDto,
            @AuthenticationPrincipal JwtUserInfoDto userInfo
    ) {
        Long userId = Long.valueOf(userInfo.getUserId());
        FileResponse response = fileService.updateFileName(fileId, requestDto, userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<MessageResponse> deleleFile(
            @PathVariable Long projectId,
            @PathVariable Long fileId,
            @AuthenticationPrincipal JwtUserInfoDto userInfo
    ) {
        Long userId = Long.valueOf(userInfo.getUserId());
        fileService.deleteFile(fileId, userId);
        return ResponseEntity.ok(new MessageResponse("success"));
    }

    @PatchMapping("/{fileId}/code")
    public ResponseEntity<EditorFileResponse> updateCode(
            @PathVariable Long projectId,
            @PathVariable Long fileId,
            @RequestBody @Valid CodeUpdateRequestDto requestDto
    ) {
        fileService.updateCode(fileId, requestDto);
        EditorFileResponse response = fileService.updateCode(fileId, requestDto);
        return ResponseEntity.ok(response);
    }
}
