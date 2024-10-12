package com.poten.hoohae.client.repository.querydsl;

import com.poten.hoohae.client.dto.res.BoardResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {
    Page<BoardResponseDto> findAll(Pageable pageable);
//    Page<BoardResponseDto> findAllByAgeAndCategory(Pageable pageable, long age, String category);
}