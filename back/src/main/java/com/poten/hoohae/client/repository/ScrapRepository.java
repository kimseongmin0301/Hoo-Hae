package com.poten.hoohae.client.repository;

import com.poten.hoohae.client.domain.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {

    @Query("select s.boardId from Scrap s where s.boardId = :id ")
    Long findByBoardId(@Param("id") Long id);

    long countByUserId(String userId);

    @Query("select s.category, count(s) from Scrap s where s.userId = :userId group by s.category")
    List<Object[]> countScrapByCategory(@Param("userId") String userId);
}
