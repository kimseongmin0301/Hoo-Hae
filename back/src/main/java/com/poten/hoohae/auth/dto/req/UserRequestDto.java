package com.poten.hoohae.auth.dto.req;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserRequestDto {
    private String nickname;
    private Long age;
    private Long characterId;
}
