package com.poten.hoohae.client.common;

public class Paging {
    public static int getPage(int page, long maxCount) {
        final int maxPage = (int) (maxCount / 10) + 1;
        if (page < 1) {
            page = 1;
        } else if (page > maxPage) {
            page = maxPage;
        }

        return page;
    }

    public static Boolean hasPage(int page, long total) {
        return (page * 5) < total;
    }
}
