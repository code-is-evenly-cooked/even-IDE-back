package com.evenly.evenide.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class CodeFile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String language = "javascript";

    @Lob
    private String content;

    private Boolean isLocked;
    private Boolean isEditLocked;

    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @PrePersist
    public void setUpdatedAt() {
        this.updatedAt = LocalDateTime.now();
    }
    @PreUpdate
    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }

    @Builder
    public CodeFile(String name, String language, String content, Boolean isLocked, Boolean isEditLocked, Project project) {
        this.name = name;
        this.language = language;
        this.content = content;
        this.isLocked = isLocked;
        this.isEditLocked = isEditLocked;
        this.project = project;
    }

    public void updateCode(String language, String content) {
        this.language = language;
        this.content = content;
    }

    public void updateName(String name) {
        this.name = name;
    }

}
