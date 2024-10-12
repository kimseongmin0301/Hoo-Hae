package com.poten.hoohae.client.controller;

import com.poten.hoohae.client.domain.Category;
import com.poten.hoohae.client.dto.req.CategoryRequestDto;
import com.poten.hoohae.client.dto.res.CategoryResponseDto;
import com.poten.hoohae.client.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/list")
    public ResponseEntity<List<CategoryResponseDto>> getCategoryList() {
        return ResponseEntity.ok(categoryService.getCategoryList());
    }
}
