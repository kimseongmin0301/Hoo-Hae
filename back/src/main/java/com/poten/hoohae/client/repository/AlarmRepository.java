package com.poten.hoohae.client.repository;

import com.poten.hoohae.client.domain.Alarm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    long countByUserId(String userId);

    Page<Alarm> findAllByUserIdOrderByIdDesc(Pageable page, String userId);

    List<Alarm> findByUserId(String id);

    @Modifying
    @Query("UPDATE Alarm b SET b.nickname = :newNickname, b.age = :age  WHERE b.nickname = :oldNickname")
    void updateNickname(@Param("oldNickname") String oldNickname, @Param("newNickname") String newNickname, @Param("age") Long age);
}
