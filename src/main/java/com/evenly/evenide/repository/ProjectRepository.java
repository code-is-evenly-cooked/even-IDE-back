package com.evenly.evenide.repository;

import com.evenly.evenide.entity.Project;
import com.evenly.evenide.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findAllByOwner(User owner);
}
