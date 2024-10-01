package com.poten.hoohae.auth.service;

import com.poten.hoohae.auth.domain.User;
import com.poten.hoohae.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public String returnUserId (String id) {
        Optional<User> user = userRepository.findByUserId(id);
        String userId = user.map(User::getUserId).orElse(null);

        return userId;
    }
}
