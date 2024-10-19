package com.poten.hoohae.auth.dto.req;

import lombok.*;

@Builder
@Data
@NoArgsConstructor  // 기본 생성자 추가
@AllArgsConstructor
@ToString
public class UserRequestDto {
    private String nickname;
    private Long age;
    private Long characterId;
    private Long image;
    private String token;
}
