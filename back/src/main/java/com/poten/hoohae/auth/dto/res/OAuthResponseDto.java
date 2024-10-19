package com.poten.hoohae.auth.dto.res;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OAuthResponseDto {
    private Long id;
    private String connected_at;
    private Properties properties;
    private KakaoAccount kakao_account;

    @Data
    @Builder
    public static class Properties {
        private String nickname;
        private String profile_image;
        private String thumbnail_image;
    }

    @Data
    @Builder
    public static class KakaoAccount {
        private String email;
        private Boolean has_email;
        private Boolean is_email_valid;
        private Boolean is_email_verified;
    }
}
