package com.mock.interview.user.domain;

import java.time.LocalTime;

public class UsersConst {
    public static final long DAILY_INTERVIEW_USAGE = 5;
    public static final String DAILY_LIMIT_KEY_FORMAT = "USER:%d:DAILY:INTERVIEW:LIMIT";
    public static final LocalTime DAILY_LIMIT_EXPIRED_TIME = LocalTime.of(2, 0);
}
