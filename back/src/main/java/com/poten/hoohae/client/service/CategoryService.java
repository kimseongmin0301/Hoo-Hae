package com.poten.hoohae.client.service;

import com.poten.hoohae.client.domain.Category;
import com.poten.hoohae.client.dto.req.CategoryRequestDto;
import com.poten.hoohae.client.dto.res.CategoryResponseDto;
import com.poten.hoohae.client.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryResponseDto> getCategoryList() {
        List<Category> categories = categoryRepository.findAll();

        List<CategoryResponseDto> list = categories.stream()
                .map(category -> CategoryResponseDto.builder()
                        .name(category.getName())
                        .image(category.getImage())
                        .build()) // Builder 패턴을 사용하여 생성
                .collect(Collectors.toList());

        return list;
    }
}
