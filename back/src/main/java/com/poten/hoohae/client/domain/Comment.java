package com.poten.hoohae.client.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "COMMENT")
@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "BOARD_ID")
    private Long boardId;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "NICKNAME")
    private String nickname;

    @Column(name = "BODY", columnDefinition = "TEXT")
    @Size(max = 500)
    private String body;

    @Column(name = "AGE")
    private Long age;

    @Column(name = "VOTE")
    private Long vote;

    @Column(name = "ADOPT_STATUS")
    private Long adoptStatus;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
