package com.evenly.evenide.controller;

import com.evenly.evenide.dto.JwtUserInfoDto;
import com.evenly.evenide.dto.ProjectRequestDto;
import com.evenly.evenide.dto.ProjectResponse;
import com.evenly.evenide.global.response.MessageResponse;
import com.evenly.evenide.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/projects")
    public ResponseEntity<ProjectResponse> createProject (
            @RequestBody @Valid ProjectRequestDto requestDto,
            @AuthenticationPrincipal JwtUserInfoDto userInfo
    ) {
        Long userId = Long.valueOf(userInfo.getUserId());
        ProjectResponse response = projectService.createProject(requestDto, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/editor")
    public ResponseEntity<List<ProjectResponse>> getMyProjects (
            @AuthenticationPrincipal JwtUserInfoDto userInfo
    ) {
        Long userId = Long.valueOf(userInfo.getUserId());
        List<ProjectResponse> responses = projectService.getMyProjects(userId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/projects/{sharedUuid}")
    public ResponseEntity<ProjectResponse> getProject (
            @PathVariable String sharedUuid,
            @AuthenticationPrincipal JwtUserInfoDto userInfoDto
    ) {
        Long userId = (userInfoDto == null ? null : Long.valueOf(userInfoDto.getUserId()));
        ProjectResponse response = projectService.getSharedProject(sharedUuid, userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/projects/{projectId}")
    public ResponseEntity<MessageResponse> deleteProject(
            @PathVariable Long projectId,
            @AuthenticationPrincipal JwtUserInfoDto userInfoDto
    ) {
        Long userId = Long.valueOf(userInfoDto.getUserId());
        projectService.deleteProject(projectId, userId);
        return ResponseEntity.ok(new MessageResponse("success"));
    }


}
