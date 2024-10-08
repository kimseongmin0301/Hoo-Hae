package com.poten.hoohae.client.repository;

import com.poten.hoohae.client.domain.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    Scrap findByBoardId(Long id);

    long countByUserId(String userId);
}
