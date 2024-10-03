package com.poten.hoohae.client.dto.req;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentRequestDto {
    private Long boardId;
    private Long userId;
    private String nickname;
    private Long age;
    private String body;
}
