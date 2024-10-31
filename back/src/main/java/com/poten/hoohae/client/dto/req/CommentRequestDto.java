package com.poten.hoohae.client.dto.req;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
public class CommentRequestDto {
    private Long id;
    private Long boardId;
    private String userId;
    private String nickname;
    private List<MultipartFile> image;
    private Long age;
    private String body;
}
