package com.evenly.evenide.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Memo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String content;

    private String codeSnapshot;

    private String fileId;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Memo(String content, String codeSnapshot, String fileId, User user) {
        this.content = content;
        this.codeSnapshot = codeSnapshot;
        this.fileId = fileId;
        this.user = user;
    }


    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    private void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
