package com.poten.hoohae.client.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "FILE")
@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "ORG_NAME")
    private String orgName;

    @Column(name = "LINK")
    private String link;

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
}
