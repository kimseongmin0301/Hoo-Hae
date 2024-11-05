package com.poten.hoohae.client.dto.res;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private Boolean isAdopted;
    private String nickname;
    private String category;
    private String type;
    private String createdAt;
    private List<String> images;
    private Boolean isVoted;
    private Boolean isBookmark;
    private String img;
    private String question;
    private Boolean isQuestion;

    @QueryProjection
    public BoardResponseDto(Long id, String subject, String body, String thumbnail,
                            String userId, Long age, String category, String type,
                            LocalDateTime createdAt, Long commentCnt, Long vote) {
        this.id = id;
        this.subject = subject;
        this.body = body;
        this.thumbnail = thumbnail;
        this.userId = userId;
        this.age = age;
        this.category = category;
        this.type = type;
        this.createdAt = createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.commentCnt = commentCnt;
        this.vote = vote;
    }
}
