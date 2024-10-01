package com.poten.hoohae.client.repository;

import com.poten.hoohae.client.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    long countBoardsByAge(Long age);

    long countCommentByBoardId(Long id);
}
