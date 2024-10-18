package com.poten.hoohae.auth.controller;

import com.poten.hoohae.auth.dto.req.UserRequestDto;
import com.poten.hoohae.auth.dto.res.OAuthResponseDto;
import com.poten.hoohae.auth.dto.res.Result;
import com.poten.hoohae.auth.dto.res.UserResponseDto;
import com.poten.hoohae.auth.service.OAuthService;
import com.poten.hoohae.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final OAuthService oAuthService;

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

    @GetMapping("/api/user/nickname/check")
    public ResponseEntity<Boolean> getNickCheck(@RequestParam("nickname") String nickname) {
        return ResponseEntity.ok(userService.findByNickname(nickname));
    }

    @PostMapping("/api/user/save")
    public ResponseEntity<Result> save(@RequestBody UserRequestDto dto) {
        OAuthResponseDto oauthDto = oAuthService.getUserInfo(dto.getToken()).block();

        return ResponseEntity.ok(userService.saveUser(Objects.requireNonNull(oauthDto)));
    }
}
