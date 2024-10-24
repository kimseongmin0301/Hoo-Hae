package com.poten.hoohae.client.repository;

import com.poten.hoohae.client.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findAllByType(String type);

    @Query("select i.image from Image i where i.id = :id")
    String findByImage(@Param("id") long id);

    @Query("select i.image from Image i where i.type = '스크랩' order by i.id asc")
    List<String> findByImageScrap();
    List<Image> findAllBy();

    @Query("select i.image from Image i where i.id = :id")
    String findByImage(@Param("id") Long id);
}
