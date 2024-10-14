package com.poten.hoohae.auth.dto.res;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDto {
    private Long id;
    private Long age;
    private Long characterId;
    private String nickname;
    private String profile;
}
