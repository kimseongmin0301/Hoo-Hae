package com.poten.hoohae.auth.controller;

import com.poten.hoohae.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/getUserId/{id}")
    public String getUserId(@PathVariable(name = "id") String id){

        return userService.returnUserId(id);
    }
}
