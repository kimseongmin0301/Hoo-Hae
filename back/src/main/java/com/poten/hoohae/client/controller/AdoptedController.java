package com.poten.hoohae.client.controller;

import com.poten.hoohae.client.service.AdoptedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/adopted")
@Slf4j
public class AdoptedController {
    private final AdoptedService adoptedService;

    @PostMapping("/{commentId}")
    public ResponseEntity<?> adopted(@PathVariable("commentId") Long id, Authentication authentication){

        return ResponseEntity.ok(adoptedService.adopted(id, authentication.getName()));
    }
}
