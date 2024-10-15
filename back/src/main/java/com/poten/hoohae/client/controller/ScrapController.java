package com.poten.hoohae.client.controller;

import com.poten.hoohae.client.service.ScrapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/{boardId}")
    public ResponseEntity<?> scarpBoard(@PathVariable("boardId") Long id, Authentication authentication) {
        return ResponseEntity.ok(scrapService.scrapBoard(id, authentication.getName()));
    }
}
