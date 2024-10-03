package com.poten.hoohae.client.repository;

import com.poten.hoohae.client.domain.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    @Query("select v.nickname from Vote v where v.boardId = :boardId and v.location = :location ")
    String findByNickname(@Param("boardId") Long boardId, @Param("location") String location);

    long countVoteByBoardId(Long id);

    @Query("select v from Vote v where v.boardId = :boardId and v.userId = :userId")
    Vote findByBoardIdAndUserId(@Param("boardId") Long boardId, @Param("userId") String userId);
}
