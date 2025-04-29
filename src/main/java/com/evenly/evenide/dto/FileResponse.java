package com.evenly.evenide.dto;

import com.evenly.evenide.entity.CodeFile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FileResponse {
    private Long id;
    private String name;
    private boolean isLocked;
    private boolean isEditLocked;

    public FileResponse(CodeFile file) {
        this.id = file.getId();
        this.name = file.getName();
        this.isLocked = Boolean.TRUE.equals(file.getIsLocked());
        this.isEditLocked = Boolean.TRUE.equals(file.getIsEditLocked());
    }
}
