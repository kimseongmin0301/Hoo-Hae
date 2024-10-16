package com.poten.hoohae.client.common;

public enum AlramEnum {
    ADOPTE("이 너의 답변이 힘이 됐대!"),
    LIKE("이 너의 글을 좋아해!"),
    COMMENT("이 너의 글에 댓글을 남겼어!");

    private final String msg;

    AlramEnum(String msg) {
        this.msg = msg;
    }
}
