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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectResponse createProject(ProjectRequestDto requestDto, String email, Long ownerId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!user.getId().equals(ownerId)) {
            throw new CustomException(ErrorCode.PROJECT_OWNER_NOT_YOU);
        }

        Project project = Project.builder()
                .name(requestDto.getProjectName())
                .owner(user)
                .build();

        Project saved = projectRepository.save(project);
        return new ProjectResponse(saved, user.getId());
    }

    @Transactional
    public List<ProjectResponse> getProjects(String email) {
        User owner = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return projectRepository.findAllByOwner(owner).stream()
                .map(p -> new ProjectResponse(p, owner.getId()))
                .toList();
    }

    public ProjectResponse updateProject(Long id, ProjectRequestDto requestDto, Long userId) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.PROJECT_NOT_FOUND));

        if (!project.getOwner().getId().equals(userId)) {
            throw new CustomException(ErrorCode.PROJECT_OWNER_NOT_YOU);
        }

        project.updateName(requestDto.getProjectName());
        return new ProjectResponse(project, userId);
    }

    public void deleteProject(Long id, Long userId) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.PROJECT_NOT_FOUND));

        if (!project.getOwner().getId().equals(userId)) {
            throw new CustomException(ErrorCode.PROJECT_OWNER_NOT_YOU);
        }
        projectRepository.deleteById(id);
    }
}
