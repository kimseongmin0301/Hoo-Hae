package com.poten.hoohae.client.repository;

import com.poten.hoohae.client.domain.Board;
import com.poten.hoohae.client.dto.res.BoardResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    Page<Board> findAll(Pageable pageable);

    Page<Board> findAllByAge(Pageable pageable, Long age);

    Page<Board> findByCategory(Pageable pageable, String category);

    long countBoardsByAge(long age);

    long countBoardsByCategory(String category);

    @Query("SELECT b.id, b.subject, b.thumbnail, b.body, b.age, b.userId, b.nickname, b.category, b.type, b.createdAt," +
            "COUNT(c.id) AS commentCnt, " +
            "COUNT(DISTINCT v.id) AS voteCnt, " +
            "(COUNT(DISTINCT c.id) + COUNT(DISTINCT v.id)) AS totalCount " +
            "FROM Board b " +
            "LEFT JOIN Comment c ON b.id = c.boardId " +
            "LEFT JOIN Vote v ON b.id = v.boardId " +
            "WHERE b.age = :age " +
            "GROUP BY b.id " +
            "ORDER BY (COUNT(DISTINCT c.id) + COUNT(DISTINCT v.id)) DESC")
    List<Object[]> findTop5ByAgeWithCommentAndVoteCount(@Param("age") Long age, Pageable pageable);

    @Query("SELECT b.id, b.subject, b.thumbnail, b.body, b.age, b.userId, b.nickname, b.category, b.type, b.createdAt," +
            "COUNT(c.id) AS commentCnt, " +
            "COUNT(DISTINCT v.id) AS voteCnt, " +
            "(COUNT(DISTINCT c.id) + COUNT(DISTINCT v.id)) AS totalCount " +
            "FROM Board b " +
            "LEFT JOIN Comment c ON b.id = c.boardId " +
            "LEFT JOIN Vote v ON b.id = v.boardId " +
            "GROUP BY b.id " +
            "ORDER BY (COUNT(DISTINCT c.id) + COUNT(DISTINCT v.id)) DESC")
    List<Object[]> findTop5ByCommentAndVoteCount(Pageable pageable);
}
