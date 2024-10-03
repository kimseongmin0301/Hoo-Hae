package com.poten.hoohae.client.dto.req;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VoteRequestDto {
    private Long id;
    private String userId;
    private String nickname;
    private String location;
    private Boolean type;
    private String boardId;
    private String commentId;
}
