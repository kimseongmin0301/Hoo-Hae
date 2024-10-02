package com.poten.hoohae.auth.handler;

import com.poten.hoohae.auth.provider.JwtProvider;
import com.poten.hoohae.client.domain.CustomOAuth2User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String userId = oAuth2User.getName();
        String token = jwtProvider.create(userId);

        // 쿠키 설정
        Cookie cookie = new Cookie("hoohae_jwt_token", token);
        cookie.setPath("/");
        cookie.setDomain("hoohae.com");
        cookie.setHttpOnly(false);  // 필요에 따라 true로 설정
        cookie.setSecure(true);
        cookie.setMaxAge(720);

        // SameSite 속성 설정 (Java Servlet API에서 직접 설정할 수 없으므로 응답 헤더를 사용)
        response.setHeader("Set-Cookie",
                "hoohae_jwt_token=" + token + "; Path=/; Domain=hoohae.com; HttpOnly=false; Secure=true; Max-Age=720; SameSite=None");

        // 리다이렉트
        response.sendRedirect("https://hoohae.com/main");
    }
}