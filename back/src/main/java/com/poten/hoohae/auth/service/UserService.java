package com.poten.hoohae.auth.service;

import com.poten.hoohae.auth.domain.User;
import com.poten.hoohae.auth.dto.req.UserRequestDto;
import com.poten.hoohae.auth.dto.res.OAuthResponseDto;
import com.poten.hoohae.auth.dto.res.Result;
import com.poten.hoohae.auth.dto.res.UserResponseDto;
import com.poten.hoohae.auth.provider.JwtProvider;
import com.poten.hoohae.auth.repository.UserRepository;
import com.poten.hoohae.client.domain.Image;
import com.poten.hoohae.client.dto.res.ImageDto;
import com.poten.hoohae.client.repository.ImageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final ImageRepository imageRepository;
    private final JwtProvider jwtProvider;

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
                .email(userId)
                .age(dto.getAge())
                .nickname(dto.getNickname())
                .characterId(dto.getCharacterId())
                .userId(userOptional.get().getUserId())
                .role("ROLE_USER")
                .createdAt(userOptional.get().getCreatedAt())
                .build();
        userRepository.save(user);

        return userRepository.save(user).getId();
    }

    public List<String> getImageList(String type){
        List<Image> image;
        if(type != null) {
            image = imageRepository.findAllByType(type);
        } else {
            image = imageRepository.findAll();
        }
        List<String> list = image.stream().map(Image::getImage).toList();

        return list;
    }

    public UserResponseDto getUserProfile(String email){
        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user = optionalUser.get();

        return UserResponseDto.builder()
                .id(user.getId())
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .characterId(user.getCharacterId())
                .profile(imageRepository.findByImage(user.getCharacterId()))
                .age(user.getAge())
                .build();
    }

    public boolean findByNickname(String nickname) {
       Optional<User> userOptional = userRepository.findByNickname(nickname);
        return userOptional.isEmpty();
    }

    @Transactional
    public Result saveUser(OAuthResponseDto dto) {
        String userId = "kakao_" + dto.getId();
        String role, token;
        long id = 0;
        Optional<User> existingUser = userRepository.findByUserId(userId);

        if (existingUser.isEmpty()) {
            User user = User.builder()
                    .userId(userId)
                    .email(dto.getKakao_account().getEmail())
                    .build();
           id = userRepository.save(user).getId();
        }

        if(id == 0 ){
            role = "ROLE_USER";
            token = jwtProvider.create(dto.getKakao_account().getEmail(), role);

            Result result = Result.builder()
                    .token(token)
                    .role("유저")
                    .build();

            return result;
        } else {
            role = "ROLE_TEMP";
            token = jwtProvider.create(dto.getKakao_account().getEmail(), role);

            Result result = Result.builder()
                    .token(token)
                    .role("임시")
                    .build();

            return result;
        }
    }

    @Transactional
    public Result onBoarding(UserRequestDto dto, String email){
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (!userOptional.isPresent()) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }
        User user = User.builder()
                .id(userOptional.get().getId())
                .email(email)
                .age(dto.getAge())
                .nickname(dto.getNickname())
                .characterId(dto.getCharacterId())
                .userId(userOptional.get().getUserId())
                .role("ROLE_USER")
                .createdAt(userOptional.get().getCreatedAt())
                .build();
        userRepository.save(user);

        String token = jwtProvider.create(email, "ROLE_USER");

        return Result.builder()
                .role("유저")
                .token(token)
                .build();
    }
}
