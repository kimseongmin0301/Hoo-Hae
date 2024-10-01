package com.poten.hoohae.client.repository;

import com.poten.hoohae.client.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    Page<Board> findAll(Pageable pageable);

    Page<Board> findAllByAge(Pageable pageable, Long age);

    long countBoardsByAge(long age);
}
