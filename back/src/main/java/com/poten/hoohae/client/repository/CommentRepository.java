package com.poten.hoohae.client.repository;

import com.poten.hoohae.client.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    long countBoardsByAge(Long age);

    long countCommentByBoardId(Long id);
    Page<Comment> findByBoardId(Pageable pageable, @Param(value = "boardId") Long boardId);

    @Query("select count(c) from Comment c where c.boardId = :boardId and c.id <= :commentId")
    long findIndexByBoardIdAndCommentId(@Param("boardId") Long boardId, @Param("commentId") Long commentId);

    List<Comment> findByUserId(String id);

    @Modifying
    @Query("UPDATE Comment b SET b.nickname = :newNickname, b.age = :age WHERE b.nickname = :oldNickname")
    void updateNickname(@Param("oldNickname") String oldNickname, @Param("newNickname") String newNickname, @Param("age") Long age);
}
