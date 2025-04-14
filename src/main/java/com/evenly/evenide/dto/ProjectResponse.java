package com.evenly.evenide.dto;

import com.evenly.evenide.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class ProjectResponse {
    private Long id;
    private String projectName;
    private String sharedUUID;
    private Long ownerId;
    private Boolean isOwner;
    private LocalDateTime createdAt;
    private List<FileResponse> files;

    public ProjectResponse(Project project, Long currentUserId) {
        this.id = project.getId();
        this.projectName = project.getName();
        this.sharedUUID = project.getSharedUuid();
        this.ownerId = project.getOwner() != null ? project.getOwner().getId() : null;
        this.isOwner = ownerId != null && ownerId.equals(currentUserId);
        this.createdAt = project.getCreatedAt();
        this.files = project.getFiles().stream()
                .map(FileResponse::new)
                .toList();
    }
}
