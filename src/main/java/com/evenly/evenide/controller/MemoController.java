package com.evenly.evenide.controller;

import com.evenly.evenide.dto.*;
import com.evenly.evenide.global.response.MessageResponse;
import com.evenly.evenide.service.MemoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects/{projectId}/file/{fileId}")
public class MemoController {

    private final MemoService memoService;

    // 메모 생성
    @PostMapping("/memos")
    public ResponseEntity<MemoResponse> createMemo(
            @PathVariable Long projectId,
            @PathVariable Long fileId,
            @RequestBody @Valid MemoRequest request,
            @AuthenticationPrincipal JwtUserInfoDto userInfo) {
        MemoResponse response = memoService.createMemo(
                projectId,
                fileId,
                request,
                Long.parseLong(userInfo.getUserId())
        );
        return ResponseEntity.ok(response);
    }


    // 메모 전체 조회
    @GetMapping("/memos")
    public ResponseEntity<List<MemoResponse>> getMemos(@PathVariable Long projectId,
                                                       @PathVariable Long fileId) {
        List<MemoResponse> responses = memoService.getAllMemos(projectId, fileId);
        return ResponseEntity.ok(responses);
    }


    // 메모 단일 조회
    @GetMapping("/memos/{memoId}")
    public ResponseEntity<MemoResponse> getMemo(@PathVariable Long projectId,
                                                      @PathVariable Long fileId,
                                                      @PathVariable Long memoId) {
        MemoResponse response = memoService.getMemo(projectId, fileId,memoId);
        return ResponseEntity.ok(response);
    }


    // 메모 수정
    @PatchMapping("/memos/{memoId}")
    public ResponseEntity<MemoResponse> updateMemo(
            @PathVariable Long projectId,
            @PathVariable Long fileId,
            @PathVariable Long memoId,
            @RequestBody MemoRequest request,
            @AuthenticationPrincipal JwtUserInfoDto userInfo) {
        MemoResponse response = memoService.updateMemo(
                projectId,
                fileId,
                memoId,
                request,
                Long.parseLong(userInfo.getUserId()));
        return ResponseEntity.ok(response);
    }

    // 메모 삭제
    @DeleteMapping("/memos/{memoId}")
    public ResponseEntity<MessageResponse> deleteMemo(@PathVariable Long projectId,
                                                      @PathVariable Long fileId,
                                                      @PathVariable Long memoId,
                                                      @AuthenticationPrincipal JwtUserInfoDto userInfo) {
        memoService.deleteMemo(
                projectId,
                fileId,
                memoId,
                Long.parseLong(userInfo.getUserId()));
        return ResponseEntity.ok(new MessageResponse("success"));
    }

}
