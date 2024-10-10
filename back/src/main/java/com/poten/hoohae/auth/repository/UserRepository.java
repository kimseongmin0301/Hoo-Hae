package com.poten.hoohae.auth.repository;

import com.poten.hoohae.auth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String id);

    Optional<User> findById(Long id);
    Optional<User> findByUserId(String id);
}
