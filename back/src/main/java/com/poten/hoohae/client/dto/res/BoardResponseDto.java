package com.poten.hoohae.client.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardResponseDto {
    private Long id;
    private String subject;
    private String body;
    private Long vote;
    private Long commentCnt;
    private String thumbnail;
    private String userId;
    private Long age;
    private Boolean isAdopte;
    private String nickname;
    private String category;
    private String type;
    private String createdAt;
    private List<String> images;
    private Boolean isVoted;
    private Boolean isBookmark;
}
