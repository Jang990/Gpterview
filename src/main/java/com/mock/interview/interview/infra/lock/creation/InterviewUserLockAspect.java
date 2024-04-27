package com.mock.interview.interview.infra.lock.creation;

import com.mock.interview.global.security.AuthenticationFinder;
import com.mock.interview.global.security.dto.LoginUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class InterviewUserLockAspect {
    private final StringRedisTemplate stringRedisTemplate;
    private final AuthenticationFinder authenticationFinder;
    private final String KEY_FORMAT = "INTERVIEW:%d:CREATE";
    private final String LOCK_VALUE = "LOCK";
    private final long LOCK_TIMEOUT_MS = 5_000;

    @Around("@within(com.mock.interview.interview.infra.lock.creation.InterviewCreationUserLock) " +
            "|| @annotation(com.mock.interview.interview.infra.lock.creation.InterviewCreationUserLock)")
    public Object checkTime(ProceedingJoinPoint pjp) throws Throwable {
        LoginUser loginUser = authenticationFinder.findAuthenticatedUser();
        String key = createKey(loginUser);
        try {
            if(!lock(key))
                // 여러 사용자가 몰리는 부분이 아님. 대기하지 않고 바로 예외를 반환함
                throw new AlreadyProceedingInterviewCreation();
            return pjp.proceed();
        } finally {
            release(key);
        }
    }

    private String createKey(LoginUser loginUser) {
        long loginId = loginUser.getId();
        return String.format(KEY_FORMAT, loginId);
    }

    private void release(String key) {
        stringRedisTemplate.delete(key);
    }

    private Boolean lock(String key) {
        return stringRedisTemplate.opsForValue()
                .setIfAbsent(
                        key,
                        LOCK_VALUE,
                        Duration.ofMillis(LOCK_TIMEOUT_MS)
                );
    }
}
