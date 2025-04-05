package com.openmpy.taleswiki.common.util;

import java.time.LocalDateTime;

public class DateFormatterUtil {

    private DateFormatterUtil() {
        throw new IllegalStateException("유틸리티 클래스입니다.");
    }

    public static String convert(final LocalDateTime localDateTime) {
        final int year = localDateTime.getYear();
        final int month = localDateTime.getMonth().getValue();
        final int day = localDateTime.getDayOfMonth();

        final int hour = localDateTime.getHour();
        final int minute = localDateTime.getMinute();
        final int second = localDateTime.getSecond();

        return String.format("%d년 %02d월 %02d일 %02d시 %02d분 %02d초", year, month, day, hour, minute, second);
    }
}
