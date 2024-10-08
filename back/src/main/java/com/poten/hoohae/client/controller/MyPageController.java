package com.poten.hoohae.client.controller;

import com.poten.hoohae.client.dto.req.MyPageRequestDto;
import com.poten.hoohae.client.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping()
    public ResponseEntity<MyPageRequestDto> getMyPageData(Authentication authentication){

        return ResponseEntity.ok(myPageService.getMyPage(authentication.getName()));
    }
}
