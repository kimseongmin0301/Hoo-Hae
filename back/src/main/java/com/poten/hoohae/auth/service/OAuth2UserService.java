package com.poten.hoohae.auth.service;

import com.poten.hoohae.auth.domain.User;
import com.poten.hoohae.auth.repository.UserRepository;
import com.poten.hoohae.client.domain.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    /**
     * OAuth 설정
     * @param req the user request
     * @return
     * @throws OAuth2AuthenticationException
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest req) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(req);
        Map<String, Object> kakaoAccount = (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");
        String email = (String) kakaoAccount.get("email");
        String userId = "kakao_" + oAuth2User.getAttributes().get("id");


        Optional<User> existingUser = userRepository.findByUserId(userId);

        if (existingUser.isEmpty()) {
            User user = User.builder()
                    .userId(userId)
                    .email(email)
                    .build();
            userRepository.save(user);
        }

        return new CustomOAuth2User(email);
    }
}