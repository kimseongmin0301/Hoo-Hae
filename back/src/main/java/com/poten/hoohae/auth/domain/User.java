package com.poten.hoohae.auth.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "USER")
@Entity
@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USER_ID", unique = true)
    private String userId;

    @Column(name = "NICKNAME")
    private String nickname;

    @Column(name = "AGE")
    private Long age;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "STATUS")
    private Long status;

    @Column(name = "ROLE")
    @Builder.Default
    private String role = "ROLE_TEMP";

    @Column(name = "CHARACTER_ID")
    private Long characterId;

    @Column(name = "REFRESH_TOKEN")
    private String refreshToken;

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
