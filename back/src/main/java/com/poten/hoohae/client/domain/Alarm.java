package com.poten.hoohae.client.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "ALARM")
@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Alarm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "NICKNAME")
    private String nickname;

    @Column(name = "BODY", columnDefinition = "TEXT")
    @Size(max = 500)
    private String body;

    @Column(name = "MSG")
    private String msg;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "BOARD_ID")
    private Long boardId;

    @Column(name = "COMMENT_ID")
    private Long commentId;

    @Column(name = "AGE")
    private Long age;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "STATUS")
    private int status;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        status = 1;
    }
}
