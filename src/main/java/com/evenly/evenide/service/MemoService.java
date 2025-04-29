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

    private final CodeFileRepository codeFileRepository;
    private final UserRepository userRepository;
    private final MemoRepository memoRepository;


    // 메모 생성
    public MemoResponse createMemo(Long projectId, Long fileId, MemoRequest request, Long userId) {
        validateProjectAndFile(projectId, fileId);
        User user = getUser(userId);
        CodeFile codeFile = getCodeFile(fileId);

        Memo memo = Memo.builder()
                .memo(request.getMemo())
                .user(user)
                .codeFile(codeFile)
                .build();

        Memo saved = memoRepository.save(memo);

        return toResponse(saved);

    }


    // 메모 전체 조회
    public List<MemoResponse> getAllMemos(Long projectId, Long fileId) {
        validateProjectAndFile(projectId, fileId);

        List<Memo> memos = memoRepository.findAllByCodeFile_IdIn(List.of(fileId));

        return memos.stream().map(this::toResponse)
                .toList();
    }



    // 메모 단건 조회
    @Transactional
    public MemoResponse getMemo(Long projectId, Long fileId,Long memoId) {
        validateProjectAndFile(projectId, fileId);

        Memo memo = memoRepository.findByMemoId(memoId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMO_NOT_FOUND));

        if (!memo.getCodeFile().getId().equals(fileId)) {
            throw new CustomException(ErrorCode.INVALID_MEMO_ACCESS);
        }

        return toResponse(memo);
    }


    // 메모 수정
    @Transactional
    public MemoResponse updateMemo(Long projectId, Long fileId, Long memoId, MemoRequest request, Long userId) {
        validateProjectAndFile(projectId, fileId);
        User user = getUser(userId);

        Memo memo = memoRepository.findByMemoIdAndUser(memoId,user)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMO_NO_PERMISSION_PATCH));

        if (!memo.getCodeFile().getId().equals(fileId)) {
            throw new CustomException(ErrorCode.INVALID_MEMO_ACCESS);
        }

        memo.update(request.getMemo());

        return toResponse(memo);
    }


    // 메모 삭제
    public void deleteMemo(Long projectId, Long fileId, Long memoId, Long userId) {
        validateProjectAndFile(projectId, fileId);
        User user = getUser(userId);
        Memo memo = memoRepository.findByMemoIdAndUser(memoId,user)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMO_NO_PERMISSION_DELETE));

        if (!memo.getCodeFile().getId().equals(fileId)) {
            throw new CustomException(ErrorCode.INVALID_MEMO_ACCESS);
        }

        memoRepository.delete(memo);
    }


    // 프로젝트, 파일 유효성 검사
    private void validateProjectAndFile(Long projectId, Long fileId) {
       if (!codeFileRepository.existsByProjectId(projectId)){
           throw new CustomException(ErrorCode.PROJECT_NOT_FOUND);
       }

       CodeFile codeFile = codeFileRepository.findById(fileId)
               .orElseThrow(() -> new CustomException(ErrorCode.FILE_NOT_FOUND));


       if (!codeFile.getProject().getId().equals(projectId)) {
           throw new CustomException(ErrorCode.INVALID_PROJECT_ACCESS);
        }
       }

        private User getUser(Long userId) {
            return userRepository.findById(userId)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        }

        private CodeFile getCodeFile(Long fileId) {
            return codeFileRepository.findById(fileId)
                    .orElseThrow(() -> new CustomException(ErrorCode.FILE_NOT_FOUND));
        }

        private MemoResponse toResponse(Memo memo) {
            return MemoResponse.builder()
                    .memoId(memo.getMemoId())
                    .memo(memo.getMemo())
                    .writerId(memo.getUser().getId())
                    .writerNickName(memo.getUser().getNickname())
                    .build();
        }
    }