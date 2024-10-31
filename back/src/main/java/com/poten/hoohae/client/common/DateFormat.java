package com.poten.hoohae.client.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateFormat {
    public static String yyyyMMdd(LocalDateTime date) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd(E) HH:mm", Locale.KOREAN);
        return date.format(dateFormatter);
    }
}
