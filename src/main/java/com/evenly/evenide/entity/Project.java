package com.evenly.evenide.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class Project {

    @Id @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column(unique = true, updatable = false)
    private String sharedUuid;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CodeFile> files = new ArrayList<>();

    @PrePersist
    public void init() {
        if (this.sharedUuid == null) {
            this.sharedUuid = UUID.randomUUID().toString();
            this.createdAt = LocalDateTime.now();
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
