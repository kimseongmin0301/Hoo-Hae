package com.poten.hoohae.client.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "VOTE")
@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "NICKNAME")
    private String nickname;

    @Column(name = "LOCATION")
    private String location;

    @Column(name = "BOARD_ID")
    private Long boardId;

    @Column(name = "COMMENT_ID")
    private Long commentId;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }


    public VoteBuilder toBuilder() {
        return Vote.builder()
                .userId(this.userId)
                .nickname(this.nickname)
                .location(this.location)
                .boardId(this.boardId)
                .commentId(this.commentId);
    }
}
