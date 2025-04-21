package com.evenly.evenide.controller;

import com.evenly.evenide.dto.*;
import com.evenly.evenide.global.response.MessageResponse;
import com.evenly.evenide.service.MemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/memo")
public class MemoController {

    private final MemoService memoService;

    // 메모 생성
    @PostMapping
    public ResponseEntity<MemoCreateResponse> createMemo(@RequestBody MemoCreateRequest request, @RequestParam int lineNumber,
                                                      @AuthenticationPrincipal JwtUserInfoDto userInfo) {
        Long memoId = memoService.createMemo(request, Long.parseLong(userInfo.getUserId()), lineNumber);
        return ResponseEntity.ok(
                new MemoCreateResponse(memoId,"success")
        );
    }

    @PostMapping("/lookup")
    public ResponseEntity<MemoLookupResponse> lookupMemo(@RequestBody MemoLookupRequest request) {
        return ResponseEntity.ok(memoService.lookup(request));
    }

    // 메모 수정
    @PatchMapping("/{id}")
    public ResponseEntity<MessageResponse> updateMemo(@PathVariable Long id, @AuthenticationPrincipal JwtUserInfoDto userInfo,
                                           @RequestBody MemoUpdateRequest request) {
        memoService.updateMemo(id, userInfo.getUserId(), request);
        return ResponseEntity.ok(new MessageResponse("success"));
    }

    // 메모 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteMemo(@PathVariable Long id, @AuthenticationPrincipal JwtUserInfoDto userInfo) {
        memoService.deleteMemo(id, userInfo.getUserId());
        return ResponseEntity.ok(new MessageResponse("success"));
    }
}
