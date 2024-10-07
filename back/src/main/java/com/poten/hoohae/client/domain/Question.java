package com.poten.hoohae.client.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "QUESTION")
@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "BODY")
    private String body;

    @Column(name = "CATEGORY")
    private String category;

    @Column(name = "COUNT")
    private Long count;
}
