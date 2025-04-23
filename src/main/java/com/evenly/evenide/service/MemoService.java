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
    public MemoCreateResponse createMemo(Long projectId,Long fileId, MemoCreateRequest request, Long userId) {
        vaildateProjectAndFile(projectId, fileId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND_NEVER));

        Memo memo = Memo.builder()
                .fileId(fileId)
                .memo(request.getMemo())
                .user(user)
                .build();

        Memo saved = memoRepository.save(memo);

        return MemoCreateResponse.builder()
                .memoId(saved.getMemoId())
                .memo(saved.getMemo())
                .writerId(saved.getUser().getId())
                .writerNickName(saved.getUser().getNickname())
                .build();

    }



    // 메모 전체 조회
    public List<MemoResponse> getAllMemos(Long projectId, Long fileId) {
//        List<Long> fileIds = codeFileRepository.findAllByProjectId(projectId).stream()
//                .map(CodeFile::getId)
//                .toList();
        vaildateProjectAndFile(projectId, fileId);

        List<Memo> memos = memoRepository.findAllByFileIdInFetchUser(List.of(fileId));

//        if (fileIds.isEmpty()) {
//            throw new CustomException(ErrorCode.PROJECT_HAS_NO_FILES);
//        }

        return memos.stream().map(memo -> MemoResponse.builder()
                        .memoId(memo.getMemoId())
                        .fileId(memo.getFileId())
                        .fileName(getFileNameById(memo.getFileId()))
                        .memo(memo.getMemo())
                        .writerId(memo.getUser().getId())
                        .writerNickName(memo.getUser().getNickname())
                        .build())
                .toList();
    }

    private String getFileNameById(Long  fileId) {
        return codeFileRepository.findById(fileId)
                .map(CodeFile::getName)
                .orElse("알수없음");
    }



    // 메모 단건 조회
    @Transactional
    public MemoSimpleResponse getMemo(Long projectId, Long fileId,Long memoId) {
        vaildateProjectAndFile(projectId, fileId);

        Memo memo = memoRepository.findWithUserByMemoId(memoId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMO_NOT_FOUND));
        return MemoSimpleResponse.builder()
                .memoId(memo.getMemoId())
                .memo(memo.getMemo())
                .writerId(memo.getUser().getId())
                .writerNickName(memo.getUser().getNickname())
                .build();
    }



    // 메모 수정
    @Transactional
    public MemoSimpleResponse updateMemo(Long projectId, Long fileId, Long memoId, MemoUpdateRequest request, Long userId) {
        vaildateProjectAndFile(projectId, fileId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND_NEVER));

        Memo memo = memoRepository.findByMemoIdAndUser(memoId,user)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMO_NO_PERMISSION_PATCH));

        memo.update(request.getMemo());

        return MemoSimpleResponse.builder()
                .memoId(memo.getMemoId())
                .memo(memo.getMemo())
                .writerId(memo.getUser().getId())
                .writerNickName(memo.getUser().getNickname())
                .build();
    }



    // 메모 삭제
    public void deleteMemo(Long projectId, Long fileId, Long memoId, Long userId) {
        vaildateProjectAndFile(projectId, fileId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND_NEVER));
        Memo memo = memoRepository.findByMemoIdAndUser(memoId,user)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMO_NO_PERMISSION_DELETE));

        memoRepository.delete(memo);
    }

    // 프로젝트, 파일 유효성 검사
    private void vaildateProjectAndFile(Long projectId, Long fileId) {
       if (!codeFileRepository.existsByProjectId(projectId)){
           throw new CustomException(ErrorCode.PROJECT_NOT_FOUND);
       }
       if (!codeFileRepository.existsById(fileId)){
           throw new CustomException(ErrorCode.FILE_NOT_FOUND);
       }

       codeFileRepository.findById(fileId).ifPresent(codeFile -> {
           if (!codeFile.getProject().getId().equals(projectId)) {
               throw new CustomException(ErrorCode.PROJECT_HAS_NO_FILES);
           }
       });
    }




}
