package com.poten.hoohae.client.repository;

import com.poten.hoohae.client.domain.Alram;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlramRepository extends JpaRepository<Alram, Long> {
    long countByUserId(String userId);

    Page<Alram> findAllByUserIdOrderByIdAsc(Pageable page, String userId);
}
