package com.poten.hoohae.client.controller;

import com.poten.hoohae.client.service.AdoptedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/adopted")
@Slf4j
public class AdoptedController {
    private final AdoptedService adoptedService;

    @PostMapping("/{commentId}")
    public ResponseEntity<?> adopted(@RequestParam("commentId") Long id, Authentication authentication){

        return ResponseEntity.ok(adoptedService.adopted(id, authentication.getName()));
    }
}
