package com.poten.hoohae.client.service;

import com.poten.hoohae.client.domain.Category;
import com.poten.hoohae.client.dto.req.CategoryRequestDto;
import com.poten.hoohae.client.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<String> getCategoryList() {
        List<Category> category = categoryRepository.findAll();

        List<String> list = category.stream()
                .map(Category::getName)
                .collect(Collectors.toList());

        return list;
    }
}
