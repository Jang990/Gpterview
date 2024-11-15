package com.mock.interview.interview;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TimeUtils {
    private static final LocalDate now = LocalDate.now();

    public static LocalDateTime time(int hour, int minute) {
        return LocalDateTime.of(now, LocalTime.of(hour, minute));
    }

    public static LocalDateTime time(int minute) {
        return LocalDateTime.of(now, LocalTime.of(0, minute));
    }
}
