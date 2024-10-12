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

    private String image;

    private String type;
}
