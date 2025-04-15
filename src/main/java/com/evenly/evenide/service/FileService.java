package com.evenly.evenide.service;

import com.evenly.evenide.dto.*;
import com.evenly.evenide.entity.CodeFile;
import com.evenly.evenide.entity.Project;
import com.evenly.evenide.global.exception.CustomException;
import com.evenly.evenide.global.exception.ErrorCode;
import com.evenly.evenide.repository.CodeFileRepository;
import com.evenly.evenide.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileService {

    private final CodeFileRepository fileRepository;
    private final ProjectRepository projectRepository;

    // 파일 생성 -> 에디터 진입 - 로그인 + 오너
    public EditorFileResponse createFile(Long projectId, FileRequestDto requestDto, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new CustomException(ErrorCode.PROJECT_NOT_FOUND));

        String name = (requestDto.getFileName()) == null || requestDto.getFileName().isBlank()
                ? "파일"
                : requestDto.getFileName();

        if (!project.getOwner().getId().equals(userId)) {
            throw new CustomException(ErrorCode.PROJECT_OWNER_NOT_YOU);
        }

        CodeFile file = CodeFile.builder()
                .name(name)
                .language("javascript")
                .content("")
                .isLocked(false)
                .isEditLocked(false)
                .project(project)
                .build();

        CodeFile saved = fileRepository.save(file);
        return new EditorFileResponse(saved);

    }

    //파일 단건 조회 -> 에디터 진입 로그인/비로그인
    @Transactional
    public EditorFileResponse getFile(Long projectId, Long fileId, Long userId) {
        CodeFile file = fileRepository.findById(fileId)
                .orElseThrow(() -> new CustomException(ErrorCode.FILE_NOT_FOUND));

        Project project = file.getProject();
        Long ownerId = project.getOwner() != null ? project.getOwner().getId() : null;

        if (!file.getProject().getId().equals(projectId)) {
            throw new CustomException(ErrorCode.INVALID_PROJECT_ACCESS);
        }

        if (file.getIsLocked() && (userId == null || !userId.equals(ownerId))) {
            throw new CustomException(ErrorCode.FILE_IS_LOCKED);
        }

        return new EditorFileResponse(file);
    }

    //파일 수정 - 로그인 + 오너
    @Transactional
    public FileResponse updateFileName(Long projectId, Long fileId, FileRequestDto requestDto, Long userId) {
        CodeFile file = fileRepository.findById(fileId)
                .orElseThrow(() -> new CustomException(ErrorCode.FILE_NOT_FOUND));

        if (!file.getProject().getId().equals(projectId)) {
            throw new CustomException(ErrorCode.INVALID_PROJECT_ACCESS);
        }

        if (!file.getProject().getOwner().getId().equals(userId)) {
            throw new CustomException(ErrorCode.PROJECT_OWNER_NOT_YOU);
        }

        file.updateName(requestDto.getFileName());
        return new FileResponse(file);
    }

    //파일 삭제 - soft 아님 - 로그인 + 오너
    @Transactional
    public void deleteFile(Long projectId, Long fileId, Long userId) {
        CodeFile file = fileRepository.findById(fileId)
                .orElseThrow(() -> new CustomException(ErrorCode.FILE_NOT_FOUND));

        if (!file.getProject().getId().equals(projectId)) {
            throw new CustomException(ErrorCode.INVALID_PROJECT_ACCESS);
        }

        if (!file.getProject().getOwner().getId().equals(userId)) {
            throw new CustomException(ErrorCode.PROJECT_OWNER_NOT_YOU);
        }

        fileRepository.delete(file);
    }

    //코드 수정 - 로그인/비로그인
    @Transactional
    public EditorFileResponse updateCode(Long projectId, Long fileId, CodeExecutionRequestDto requestDto) {
        CodeFile file = fileRepository.findById(fileId)
                .orElseThrow(() -> new CustomException(ErrorCode.FILE_NOT_FOUND));

        if (!file.getProject().getId().equals(projectId)) {
            throw new CustomException(ErrorCode.INVALID_PROJECT_ACCESS);
        }

        if (file.getIsEditLocked()) {
            throw new CustomException(ErrorCode.CODE_EDIT_LOCKED);
        }
        file.updateCode(requestDto.getLanguage(), requestDto.getContent());
        return new EditorFileResponse(file);
    }

    //파일 잠금, 해제
    @Transactional
    public FileResponse updateLock(Long projectId, Long fileId, Long userId) {
        CodeFile file = fileRepository.findById(fileId)
                .orElseThrow(() -> new CustomException(ErrorCode.FILE_NOT_FOUND));

        if (!file.getProject().getId().equals(projectId)) {
            throw new CustomException(ErrorCode.INVALID_PROJECT_ACCESS);
        }

        if (!file.getProject().getOwner().getId().equals(userId)) {
            throw new CustomException(ErrorCode.PROJECT_OWNER_NOT_YOU);
        }

        file.updateLock();
        return new FileResponse(file);
    }

    //코드 수정 잠금, 해제
    @Transactional
    public FileResponse updateEditLock(Long projectId, Long fileId, Long userId) {
        CodeFile file = fileRepository.findById(fileId)
                .orElseThrow(() -> new CustomException(ErrorCode.FILE_NOT_FOUND));

        if (!file.getProject().getId().equals(projectId)) {
            throw new CustomException(ErrorCode.INVALID_PROJECT_ACCESS);
        }

        if (!file.getProject().getOwner().getId().equals(userId)) {
            throw new CustomException(ErrorCode.PROJECT_OWNER_NOT_YOU);
        }

        file.updateEditLock();
        return new FileResponse(file);
    }
}
