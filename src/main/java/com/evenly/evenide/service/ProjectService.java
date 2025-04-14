package com.evenly.evenide.service;

import com.evenly.evenide.dto.ProjectRequestDto;
import com.evenly.evenide.dto.ProjectResponse;
import com.evenly.evenide.entity.Project;
import com.evenly.evenide.entity.User;
import com.evenly.evenide.global.exception.CustomException;
import com.evenly.evenide.global.exception.ErrorCode;
import com.evenly.evenide.repository.ProjectRepository;
import com.evenly.evenide.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    //로그인 사용자 프로젝트 생성
    public ProjectResponse createProject(ProjectRequestDto requestDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!user.getId().equals(requestDto.getOwnerId())) {
            throw new CustomException(ErrorCode.PROJECT_OWNER_NOT_YOU);
        }

        Project project = Project.builder()
                .name(requestDto.getProjectName())
                .owner(user)
                .build();

        Project saved = projectRepository.save(project);
        return new ProjectResponse(saved, user.getId());
    }

    //기본 프로젝트 생성 - 비로그인 전용
    // 추후 추가

    // 내 전체 프로젝트 조회 (사이드바용)
    @Transactional
    public List<ProjectResponse> getMyProjects(Long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return projectRepository.findAllByOwner(owner).stream()
                .map(p -> new ProjectResponse(p, owner.getId()))
                .collect(Collectors.toList());
    }

    // 공유 링크 프로젝트 단건 조회
    @Transactional
    public ProjectResponse getSharedProject(String uuid, Long userId) {
        Project project = projectRepository.findBySharedUuid(uuid)
                .orElseThrow(() -> new CustomException(ErrorCode.PROJECT_NOT_FOUND));

        return new ProjectResponse(project, userId);
    }

    // 이름 수정
    public ProjectResponse updateProject(Long id, ProjectRequestDto requestDto, Long userId) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.PROJECT_NOT_FOUND));

        if (!project.getOwner().getId().equals(userId)) {
            throw new CustomException(ErrorCode.PROJECT_OWNER_NOT_YOU);
        }

        project.updateName(requestDto.getProjectName());
        return new ProjectResponse(project, userId);
    }

    // 삭제
    public void deleteProject(Long id, Long userId) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.PROJECT_NOT_FOUND));

        if (!project.getOwner().getId().equals(userId)) {
            throw new CustomException(ErrorCode.PROJECT_OWNER_NOT_YOU);
        }

        projectRepository.delete(project);
    }
}
