package com.evenly.evenide.repository;

import com.evenly.evenide.entity.CodeFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CodeFileRepository extends JpaRepository<CodeFile, Long> {
    List<CodeFile> findAllByProjectId(Long projectId);
}
