package com.poten.hoohae.auth.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor  // 기본 생성자 추가
@AllArgsConstructor
public class UserRequestDto {
    private String nickname;
    private Long age;
    private Long characterId;
    private Long image;
    private String token;
}
