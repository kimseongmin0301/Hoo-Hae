package com.poten.hoohae.client.repository;

import com.poten.hoohae.client.domain.Board;
import com.poten.hoohae.client.dto.res.BoardResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("select b, count(c) as commentCnt, count(v) as voteCnt from Board b " +
            "left join Comment c on b.id = c.boardId " +
            "left join Vote v on b.id = v.boardId " +
            "group by b")
    Page<Board> findAll(Pageable pageable);

    @Query("select count(b) from Board b where b.adoptionId is not null")
    long countBoardsByAdopted();

    Page<BoardResponseDto> findAllByAge(Pageable pageable, Long age);

    Page<BoardResponseDto> findByCategory(Pageable pageable, String category);

    Page<BoardResponseDto> findAllByAgeAndCategory(Pageable pageable, Long age, String category);

    long countBoardsByAge(long age);

    long countBoardsByCategory(String category);

    @Query("SELECT b.id, b.subject, b.thumbnail, b.body, b.age, b.userId, b.nickname, b.category, b.type, b.createdAt, b.adoptionId," +
            "COUNT(distinct c.id) AS commentCnt, " +
            "COUNT(DISTINCT v.id) AS voteCnt, " +
            "(COUNT(DISTINCT c.id) + COUNT(DISTINCT v.id)) AS totalCount " +
            "FROM Board b " +
            "LEFT JOIN Comment c ON b.id = c.boardId " +
            "LEFT JOIN Vote v ON b.id = v.boardId " +
            "WHERE b.age = :age " +
            "GROUP BY b.id " +
            "ORDER BY (COUNT(DISTINCT c.id) + COUNT(DISTINCT v.id)) DESC")
    List<Object[]> findTop5ByAgeWithCommentAndVoteCount(@Param("age") Long age, Pageable pageable);

    @Query("SELECT b.id, b.subject, b.thumbnail, b.body, b.age, b.userId, b.nickname, b.category, b.type, b.createdAt, b.adoptionId," +
            "COUNT(distinct c.id) AS commentCnt, " +
            "COUNT(DISTINCT v.id) AS voteCnt, " +
            "(COUNT(DISTINCT c.id) + COUNT(DISTINCT v.id)) AS totalCount " +
            "FROM Board b " +
            "LEFT JOIN Comment c ON b.id = c.boardId " +
            "LEFT JOIN Vote v ON b.id = v.boardId " +
            "GROUP BY b.id " +
            "ORDER BY (COUNT(DISTINCT c.id) + COUNT(DISTINCT v.id)) DESC")
    List<Object[]> findTop5ByCommentAndVoteCount(Pageable pageable);

    long countByUserIdAndAdoptionIdIsNotNull(String userId);

    long countByUserId(String userId);

    @Query("SELECT b.category, COUNT(b) FROM Board b WHERE b.userId = :userId GROUP BY b.category ORDER BY COUNT(b) DESC")
    List<Object[]> countByUserIdGroupByCategoryTop3(@Param("userId") String userId, Pageable pageable);

    List<Board> findByUserId(String id);

    @Modifying
    @Query("UPDATE Board b SET b.nickname = :newNickname WHERE b.nickname = :oldNickname")
    void updateNickname(@Param("oldNickname") String oldNickname, @Param("newNickname") String newNickname);

}
