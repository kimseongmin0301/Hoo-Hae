package com.poten.hoohae.auth.service;

import com.poten.hoohae.auth.domain.User;
import com.poten.hoohae.auth.dto.req.UserRequestDto;
import com.poten.hoohae.auth.dto.res.UserResponseDto;
import com.poten.hoohae.auth.repository.UserRepository;
import com.poten.hoohae.client.domain.Image;
import com.poten.hoohae.client.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final ImageRepository imageRepository;

    public String returnUserId (String id) {
        Optional<User> user = userRepository.findByEmail(id);
        String userId = user.map(User::getUserId).orElse(null);

        return userId;
    }

    public Long updateProfile(UserRequestDto dto, String userId) {
        Optional<User> userOptional = userRepository.findByEmail(userId);
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

    public List<Image> getImageList(String type){
        List<Image> image;
        if(type != null) {
            image = imageRepository.findAllByType(type);
        } else {
            image = imageRepository.findAll();
        }
        return image;
    }

    public UserResponseDto getUserProfile(String email){
        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user = optionalUser.get();

        return UserResponseDto.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .profile(imageRepository.findByImage(user.getCharacterId()))
                .age(user.getAge())
                .build();
    }
}
