package com.evenly.evenide.repository;

import com.evenly.evenide.entity.CodeFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodeFileRepository extends JpaRepository<CodeFile, Long> {
    boolean existsByProjectId(Long projectId);
}
