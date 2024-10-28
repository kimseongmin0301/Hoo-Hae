package com.poten.hoohae.client.repository;

import com.poten.hoohae.client.domain.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    @Query("select DISTINCT  v.nickname from Vote v where v.boardId = :boardId and v.location = :location and v.userId = :userId ")
    String findByNickname(@Param("boardId") Long boardId, @Param("location") String location, @Param("userId") String userId);

    @Query("select v.nickname from Vote v where v.commentId = :commentId and v.location = :location and v.userId = :userId ")
    String findByCommentNickname(@Param("commentId") Long commentId, @Param("location") String location, @Param("userId") String userId);

    @Query("select v.userId from Vote v where v.userId = :userId and v.boardId = :id ")
    String findByUserId(@Param("userId") String userId, @Param("id") Long id);

    @Query("select v from Vote v where v.userId = :userId")
    List<Vote> findByUser(@Param("userId") String userId);

    long countVoteByBoardId(Long id);

    long countVoteByCommentId(Long id);

    @Query("select v from Vote v where v.commentId = :commentId and v.userId = :userId")
    Optional<Vote> findByCommentIdAndUserId(@Param("commentId") Long commentId, @Param("userId") String userId);

    @Query("select v from Vote v where v.boardId = :boardId and v.userId = :userId")
    Optional<Vote> findByBoardIdAndUserId(@Param("boardId") Long boardId, @Param("userId") String userId);

    @Modifying
    @Query("UPDATE Vote b SET b.nickname = :newNickname WHERE b.nickname = :oldNickname")
    void updateNickname(@Param("oldNickname") String oldNickname, @Param("newNickname") String newNickname);
}
