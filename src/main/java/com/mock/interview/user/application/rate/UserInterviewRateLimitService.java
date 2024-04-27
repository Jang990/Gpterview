package com.mock.interview.user.application.rate;

import com.mock.interview.global.TimeDifferenceCalculator;
import com.mock.interview.global.security.AuthenticationFinder;
import com.mock.interview.global.security.dto.LoginUserDetail;
import com.mock.interview.user.domain.UsersConst;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
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
        long diffMinute = TimeDifferenceCalculator
                .calculate(ChronoUnit.MINUTES, LocalTime.now(), UsersConst.DAILY_LIMIT_EXPIRED_TIME);
        stringRedisTemplate.expire(key, Duration.ofMinutes(diffMinute));
    }

    private String createKey() {
        LoginUserDetail loginUserDetail = authenticationFinder.findCurrentAuthenticatedUser();
        return String.format(UsersConst.DAILY_LIMIT_KEY_FORMAT, loginUserDetail.getId());
    }
}
