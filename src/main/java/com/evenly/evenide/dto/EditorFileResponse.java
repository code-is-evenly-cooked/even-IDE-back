package com.evenly.evenide.dto;

import com.evenly.evenide.entity.CodeFile;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class EditorFileResponse {
    private Long fileId;
    private String filename;
    private String language;
    private String content;
    private boolean isLocked;
    private boolean isEditLocked;
    private LocalDateTime updatedAt;
    private Long ownerId;

    public EditorFileResponse(CodeFile file) {
        this.fileId = file.getId();
        this.filename = file.getName();
        this.language = file.getLanguage();
        this.content = file.getContent();
        this.isLocked = Boolean.TRUE.equals(file.getIsLocked());
        this.isEditLocked = Boolean.TRUE.equals(file.getIsEditLocked());
        this.updatedAt = file.getUpdatedAt();
        this.ownerId = file.getProject().getOwner() != null
                ? file.getProject().getOwner().getId()
                : null;
    }
}
