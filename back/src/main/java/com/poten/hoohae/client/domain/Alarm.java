package com.poten.hoohae.client.domain;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "BODY")
    private String body;

    @Column(name = "MSG")
    private String msg;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "BOARD_ID")
    private Long boardId;

    @Column(name = "COMMENT_ID")
    private Long commentId;

    @Column(name = "age")
    private Long age;
}
