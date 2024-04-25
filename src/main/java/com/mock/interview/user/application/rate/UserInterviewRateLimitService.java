package com.mock.interview.user.application.rate;

import com.mock.interview.global.security.AuthenticationFinder;
import com.mock.interview.user.domain.UsersConst;
import com.mock.interview.user.domain.model.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class UserInterviewRateLimitService {
    private final StringRedisTemplate stringRedisTemplate;
    private final AuthenticationFinder authenticationFinder;

    public long increase() {
        String key = createKey();
        Long result = stringRedisTemplate.opsForValue().increment(key);
        if(result == null)
            throw new IllegalStateException();
        if (result == 1)
            setExpiredTime(key);
        return result;
    }

    public long decrease() {
        String key = createKey();
        Long result = stringRedisTemplate.opsForValue().decrement(key);
        if(result == null)
            throw new IllegalStateException();
        if(result < 0)
            throw new IllegalStateException("0 이하일 수 없음");
        return result;
    }

    private void setExpiredTime(String key) {
        long diffMinute = calculateMinuteDifference(LocalTime.now(), UsersConst.DAILY_LIMIT_EXPIRED_TIME);
        stringRedisTemplate.expire(key, Duration.ofMinutes(diffMinute));
    }

    private String createKey() {
        Users authenticatedUser = authenticationFinder.findAuthenticatedUser();
        return String.format(UsersConst.DAILY_LIMIT_KEY_FORMAT, authenticatedUser.getId());
    }

    public static long calculateMinuteDifference(LocalTime start, LocalTime end) {
        if (start.equals(end) || start.isAfter(end)) {
            LocalDate nowDate = LocalDate.now();
            LocalDateTime startDateTime = LocalDateTime.of(nowDate, start);
            LocalDateTime endDateTime = LocalDateTime.of(nowDate.plusDays(1), end);
            return calculateMinuteDifference(startDateTime, endDateTime);
        }
        return ChronoUnit.MINUTES.between(start, end);
    }

    public static long calculateMinuteDifference(LocalDateTime start, LocalDateTime end) {

        return ChronoUnit.MINUTES.between(start, end);
    }
}
