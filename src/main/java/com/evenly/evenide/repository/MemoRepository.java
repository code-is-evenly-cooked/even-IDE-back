package com.evenly.evenide.repository;

import com.evenly.evenide.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {
    List<Memo> findAllByFileIdAndCodeSnapshot(String fileId, String codeSnapshot);
    List<Memo> findAllByFileId(String fileId);
}
