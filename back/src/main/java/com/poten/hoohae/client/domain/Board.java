package com.poten.hoohae.client.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "BOARD")
@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "NICKNAME")
    private String nickanme;

    @Column(name = "SUBJECT")
    private String subject;

    @Column(name = "BODY")
    private String body;

    @Column(name = "VOTE")
    private Long vote;

    @Column(name = "ADOPTION_ID")
    private Long adoptionId;

    @Column(name = "THUMBNAIL")
    private Long thumbnail;

    @Column(name = "AGE")
    private Long age;

    @Column(name = "CATEGORY")
    private Long category;

    @Column(name = "TYPE")
    private String type;

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
