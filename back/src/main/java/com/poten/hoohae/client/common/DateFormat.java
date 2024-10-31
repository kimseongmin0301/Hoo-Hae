package com.poten.hoohae.client.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormat {
    public static String yyyyMMdd(LocalDateTime date) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd(E) HH:mm");
        return date.format(dateFormatter);
    }
}
