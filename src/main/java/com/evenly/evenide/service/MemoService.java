package com.evenly.evenide.service;

import com.evenly.evenide.dto.*;
import com.evenly.evenide.entity.CodeFile;
import com.evenly.evenide.entity.Memo;
import com.evenly.evenide.entity.User;
import com.evenly.evenide.global.exception.CustomException;
import com.evenly.evenide.global.exception.ErrorCode;
import com.evenly.evenide.repository.CodeFileRepository;
import com.evenly.evenide.repository.MemoRepository;
import com.evenly.evenide.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemoService {

    private final MemoRepository memoRepository;
    private final UserRepository userRepository;
    private final MemoCacheService memoCacheService;
    private final CodeFileRepository codeFileRepository;

    @Transactional
    public Long createMemo(MemoCreateRequest request, Long userId, int lineNumber) {

        CodeFile codeFile = codeFileRepository.findById(Long.valueOf(request.fileId()))
                .orElseThrow(() -> new CustomException(ErrorCode.FILE_NOT_FOUND));

        String[] lines = codeFile.getContent().split("\n");
        if (lineNumber < 1 || lineNumber > lines.length) {
            throw new CustomException(ErrorCode.MEMO_NOT_FOUND);
        }
        String snapshot = lines[lineNumber - 1].trim();

        memoCacheService.saveSnapShotLine(request.fileId(), snapshot, lineNumber);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND_WHERE));

        Memo memo = new Memo(request.content(), snapshot, request.fileId(), user);
        memoRepository.save(memo);
        return memo.getId();
    }


    // 메모 목록 조회
    @Transactional(readOnly = true)
    public MemoLookupResponse lookup(MemoLookupRequest request) {
        CodeFile codeFile = codeFileRepository.findById(Long.valueOf(request.fileId()))
                .orElseThrow(() -> new CustomException(ErrorCode.FILE_NOT_FOUND));

        String[] lines = codeFile.getContent().split("\n");
        String snapshot = request.codeSnapshot().trim();

        int matchedLine = -1;
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].trim().equals(snapshot)) {
                matchedLine = i + 1;
                break;
            }
        }

        List<Memo> memos = memoRepository.findAllByFileIdAndCodeSnapshot(
                request.fileId(), snapshot
        );

        List<MemoSimpleDto> memoDtos = memos.stream()
                .map(m -> new MemoSimpleDto(m.getId(), m.getContent(), m.getCreatedAt()))
                .toList();

        return new MemoLookupResponse(matchedLine, memoDtos);

    }

    // 메모 수정
    @Transactional
    public void updateMemo(Long memoId, String userId,MemoUpdateRequest request) {
        Memo memo = memoRepository.findById(memoId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMO_NOT_FOUND));

        if (!memo.getUser().getId().equals(Long.parseLong(userId))) {
            throw new CustomException(ErrorCode.MEMO_NO_PERMISSION_PATCH);
        }
        memo.updateContent(request.content());
    }

    // 메모 삭제
    @Transactional
    public void deleteMemo(Long memoId, String userId) {
        Memo memo = memoRepository.findById(memoId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMO_NOT_FOUND));
        if (!memo.getUser().getId().equals(Long.parseLong(userId))) {
            throw new CustomException(ErrorCode.MEMO_NO_PERMISSION_DELETE);
        }
        memoRepository.delete(memo);
    }
}
