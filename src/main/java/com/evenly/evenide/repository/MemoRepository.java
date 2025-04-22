package com.evenly.evenide.repository;

import com.evenly.evenide.entity.Memo;
import com.evenly.evenide.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemoRepository extends JpaRepository<Memo, Long> {
    @Query("SELECT m FROM Memo m JOIN FETCH m.user WHERE m.fileId IN :fileIds")
    List<Memo> findAllByFileIdInFetchUser(@Param("fileIds") List<Long> fileIds);

    @Query("SELECT m FROM Memo m JOIN FETCH m.user WHERE m.memoId = :memoId")
    Optional<Memo> findWithUserByMemoId(@Param("memoId") Long memoId);

    Optional<Memo> findByMemoIdAndUser(Long memoId, User user);
}
