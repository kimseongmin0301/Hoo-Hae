package com.poten.hoohae.auth.controller;

import com.poten.hoohae.auth.dto.req.UserRequestDto;
import com.poten.hoohae.auth.dto.res.UserResponseDto;
import com.poten.hoohae.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/getUserId/{id}")
    public String getUserId(@PathVariable(name = "id") String id){

        return userService.returnUserId(id);
    }

    @PutMapping("/api/user/update/profile")
    public ResponseEntity<Long> updateProfile(@RequestBody UserRequestDto dto, Authentication authentication) {

        return ResponseEntity.ok(userService.updateProfile(dto, authentication.getName()));
    }

    @GetMapping("/api/user/profile")
    public ResponseEntity<UserResponseDto> getProfile(Authentication authentication) {

        return ResponseEntity.ok(userService.getUserProfile(authentication.getName()));
    }
}
