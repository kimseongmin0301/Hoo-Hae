package com.poten.hoohae.client.controller;

import com.poten.hoohae.client.service.ScrapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/scrap")
@RestController
@RequiredArgsConstructor
public class ScrapController {

    private final ScrapService scrapService;

    @GetMapping()
    public ResponseEntity<List<?>> getScrapImage(Authentication authentication) {

        return ResponseEntity.ok(scrapService.findByImageScrap(authentication.getName()));
    }
}
