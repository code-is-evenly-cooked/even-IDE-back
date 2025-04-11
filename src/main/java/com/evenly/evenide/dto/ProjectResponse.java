package com.evenly.evenide.dto;

import com.evenly.evenide.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProjectResponse {
    private Long id;
    private String projectName;
    private String shareToken;
    private Long ownerId;
    private Boolean isOwner;

    public ProjectResponse(Project project, Long currentUserId) {
        this.id = project.getId();
        this.projectName = project.getName();
        this.shareToken = project.getShareToken();
        this.ownerId = project.getOwner().getId();
        this.isOwner = ownerId.equals(currentUserId);
    }
}
