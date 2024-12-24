package com.poten.hoohae.client.repository;

import com.poten.hoohae.client.domain.Question;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "SELECT q.* FROM question q WHERE q.count = (SELECT MIN(q2.count) FROM question q2) ORDER BY RAND() limit 1", nativeQuery = true)
    Optional<Question> findRandomWithMinCount();

    @Modifying
    @Transactional
    @Query("UPDATE Question q SET q.count = q.count + 1 WHERE q.id = :id")
    void incrementCount(@Param("id") Long id);
}
