package com.poten.hoohae.client.controller;

import com.poten.hoohae.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/image")
public class ImageController {
    private final UserService userService;

    @GetMapping("/list")
    public ResponseEntity<List<?>> getImageList (String type) {
        return ResponseEntity.ok(userService.getImageList(type));
    }
}
