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
        EditorFileResponse response = fileService.getFile(projectId, fileId, userId);
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
        FileResponse response = fileService.updateFileName(projectId, fileId, requestDto, userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<MessageResponse> deleteFile(
            @PathVariable Long projectId,
            @PathVariable Long fileId,
            @AuthenticationPrincipal JwtUserInfoDto userInfo
    ) {
        Long userId = Long.valueOf(userInfo.getUserId());
        fileService.deleteFile(projectId, fileId, userId);
        return ResponseEntity.ok(new MessageResponse("success"));
    }

    @PatchMapping("/{fileId}/code")
    public ResponseEntity<EditorFileResponse> updateCode(
            @PathVariable Long projectId,
            @PathVariable Long fileId,
            @RequestBody @Valid CodeExecutionRequestDto requestDto
    ) {
        EditorFileResponse response = fileService.updateCode(projectId, fileId, requestDto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{fileId}/lock")
    public ResponseEntity<FileResponse> updateLock(
            @PathVariable Long projectId,
            @PathVariable Long fileId,
            @AuthenticationPrincipal JwtUserInfoDto userInfoDto
    ) {
        Long userId = Long.valueOf(userInfoDto.getUserId());
        FileResponse response = fileService.updateLock(projectId, fileId, userId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{fileId}/edit/lock")
    public ResponseEntity<FileResponse> updateEditLock(
            @PathVariable Long projectId,
            @PathVariable Long fileId,
            @AuthenticationPrincipal JwtUserInfoDto userInfoDto
    ) {
        Long userId = Long.valueOf(userInfoDto.getUserId());
        FileResponse response = fileService.updateEditLock(projectId, fileId, userId);
        return ResponseEntity.ok(response);
    }

}
