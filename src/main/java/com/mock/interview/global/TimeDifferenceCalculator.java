package com.mock.interview.global;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public final class TimeDifferenceCalculator {
    private TimeDifferenceCalculator() {}

    public static long calculate(final ChronoUnit unit, final LocalTime start, final LocalTime end) {
        if (start.equals(end) || start.isAfter(end)) {
            LocalDate nowDate = LocalDate.now();
            LocalDateTime startDateTime = LocalDateTime.of(nowDate, start);
            LocalDateTime endDateTime = LocalDateTime.of(nowDate.plusDays(1), end);
            return calculate(unit, startDateTime, endDateTime);
        }
        return unit.between(start, end);
    }

    public static long calculate(final ChronoUnit unit, final LocalDateTime start, final LocalDateTime end) {
        return unit.between(start, end);
    }
}
