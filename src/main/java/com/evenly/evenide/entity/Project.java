package com.evenly.evenide.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class Project {

    @Id @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    private LocalDateTime createdAt;

    @Column(unique = true, updatable = false)
    private String shareToken;

    @PrePersist
    public void setCreatedAt() {
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    public void setShareToken() {
        if (this.shareToken == null) {
            this.shareToken = UUID.randomUUID().toString();
        }
    }

    @Builder
    public Project(String name, User owner) {
        this.name = name;
        this.owner = owner;
    }

    public void updateName(String name) {
        this.name = name;
    }
}
