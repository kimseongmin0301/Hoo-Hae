package com.poten.hoohae.auth.service;

import com.poten.hoohae.auth.domain.User;
import com.poten.hoohae.auth.dto.req.UserRequestDto;
import com.poten.hoohae.auth.dto.res.UserResponseDto;
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

    public Long updateProfile(UserRequestDto dto, String userId) {
        Optional<User> userOptional = userRepository.findByUserId(userId);
        if (!userOptional.isPresent()) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }
        User user = User.builder()
                .id(userOptional.get().getId())
                .age(dto.getAge())
                .nickname(dto.getNickname())
                .characterId(dto.getCharacterId())
                .userId(userId)
                .createdAt(userOptional.get().getCreatedAt())
                .build();
        userRepository.save(user);

        return userRepository.save(user).getId();
    }
}
