package com.poten.hoohae.client.domain;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "IMAGE")
@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "IMAGE", columnDefinition = "TEXT")
    private String image;

    @Column(name = "TYPE")
    private String type;
}
