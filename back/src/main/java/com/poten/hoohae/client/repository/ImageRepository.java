package com.poten.hoohae.client.repository;

import com.poten.hoohae.client.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findAllByType(String type);

    List<Image> findAllBy();
}
