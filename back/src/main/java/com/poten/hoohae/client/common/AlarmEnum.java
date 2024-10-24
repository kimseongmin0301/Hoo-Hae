package com.poten.hoohae.client.common;

public enum AlarmEnum {
    ADOPTED("이 너의 답변이 힘이 됐대!"),
    LIKE("이 너의 글을 좋아해!"),
    COMMENT("이 너의 글에 댓글을 남겼어!");

    private final String msg;

    AlarmEnum(String msg) {
        this.msg = msg;
    }
}
