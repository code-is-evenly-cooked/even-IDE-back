package com.evenly.evenide.repository;

import com.evenly.evenide.entity.Memo;
import com.evenly.evenide.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemoRepository extends JpaRepository<Memo, Long> {

    Optional<Memo> findByMemoIdAndUser(Long memoId, User user);

    @EntityGraph(attributePaths = "user")
    List<Memo> findAllByCodeFile_IdIn(List<Long> fileIds);

    @EntityGraph(attributePaths = "user")
    Optional<Memo> findByMemoId(Long memoId);

}
