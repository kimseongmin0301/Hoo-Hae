package com.poten.hoohae.client.repository;

import com.poten.hoohae.client.domain.Alarm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    long countByUserId(String userId);

    Page<Alarm> findAllByUserIdOrderByIdAsc(Pageable page, String userId);
}
