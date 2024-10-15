package com.poten.hoohae.client.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "ALRAM")
@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Alram {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
