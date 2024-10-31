package com.poten.hoohae.client.dto.res;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class AlarmResponseDto {
    private Long id;
    private String body;
    private String nickname;
    private String msg;
    private String image;
    private Boolean isAlive;
    private Long age;
    private int page;
    private Long boardId;
    private LocalDateTime createdAt;
}
