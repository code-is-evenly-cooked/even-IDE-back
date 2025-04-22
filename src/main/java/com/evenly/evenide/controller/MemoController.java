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
@RequestMapping("/memo")
public class MemoController {

    private final MemoService memoService;

    // 메모 생성
    @PostMapping
    public ResponseEntity<MemoCreateResponse> createMemo(@RequestBody @Valid MemoCreateRequest request,
                                                         @AuthenticationPrincipal JwtUserInfoDto userInfo) {
        MemoCreateResponse response = memoService.createMemo(
                request,
                Long.parseLong(userInfo.getUserId())
        );
        return ResponseEntity.ok(response);
    }


    // 메모 전체 조회
    @GetMapping
    public ResponseEntity<List<MemoResponse>> getMemos(@RequestParam Long projectId) {
        List<MemoResponse> responses = memoService.getAllMemos(projectId);
        return ResponseEntity.ok(responses);
    }


    // 메모 단일 조회
    @GetMapping("/{memoId}")
    public ResponseEntity<MemoSimpleResponse> getMemo(@PathVariable Long memoId) {
        MemoSimpleResponse response = memoService.getMemo(memoId);
        return ResponseEntity.ok(response);
    }


    // 메모 수정
    @PatchMapping
    public ResponseEntity<MemoSimpleResponse> updateMemo(@RequestBody MemoUpdateRequest request,
                                                   @AuthenticationPrincipal JwtUserInfoDto userInfo) {
        MemoSimpleResponse response = memoService.updateMemo(
                request.getMemoId(),
                request,
                Long.parseLong(userInfo.getUserId()));
        return ResponseEntity.ok(response);
    }

    // 메모 삭제
    @DeleteMapping("/{memoId}")
    public ResponseEntity<MessageResponse> deleteMemo(@PathVariable Long memoId, @AuthenticationPrincipal JwtUserInfoDto userInfo) {
        memoService.deleteMemo(memoId, Long.parseLong(userInfo.getUserId()));
        return ResponseEntity.ok(new MessageResponse("success"));
    }

}
