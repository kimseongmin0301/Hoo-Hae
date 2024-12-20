package com.poten.hoohae.client.dto.res;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CommentResponseDto {
    private Long boardId;
    private String userId;
    private Long id;
    private String nickname;
    private String body;
    private Long age;
    private Long vote;
    private Boolean isWriter;
    private Boolean isAdopted;
    private String createdAt;
    private String img;
    private Boolean isVoted;
    private String commentImage;
}
