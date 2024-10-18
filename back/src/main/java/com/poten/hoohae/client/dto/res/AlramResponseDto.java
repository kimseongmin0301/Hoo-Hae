package com.poten.hoohae.client.dto.res;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AlramResponseDto {
    private Long id;
    private String body;
    private String nickname;
    private String msg;
    private String image;
    private Boolean isAlive;
}
