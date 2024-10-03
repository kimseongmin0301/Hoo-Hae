package com.poten.hoohae.client.repository;

import com.poten.hoohae.client.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

    @Query("select f.link from File f where f.boardId = :boardId")
    List<String> findByName(@Param("boardId") Long boardId);
}
