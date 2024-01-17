package com.mock.interview.interview.infrastructure.lock.creation;

import com.mock.interview.user.domain.model.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class InterviewUserLockAspect {
    private final StringRedisTemplate stringRedisTemplate;
    private final String KEY_FORMAT = "INTERVIEW:%d:CREATE";
    private final String LOCK_VALUE = "LOCK";
    private final long LOCK_TIMEOUT_MS = 5_000;

    @Around("@within(com.mock.interview.interview.infrastructure.lock.creation.InterviewUserLock) " +
            "|| @annotation(com.mock.interview.interview.infrastructure.lock.creation.InterviewUserLock)")
    public Object checkTime(ProceedingJoinPoint pjp) throws Throwable {
        Users authentication = getAuthenticatedUser();
        String key = createKey(authentication);
        try {
            if(!lock(key))
                // 여러 사용자가 몰리는 부분이 아님. 대기하지 않고 바로 예외를 반환함
                throw new AlreadyProceedingInterviewCreation();
            return pjp.proceed();
        } finally {
            release(key);
        }
    }

    private String createKey(Users authentication) {
        long loginId = authentication.getId();
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

    private Users getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || authentication instanceof AnonymousAuthenticationToken
                || !authentication.isAuthenticated())
            throw new UsernameNotFoundException("사용자 식별 불가");
        return (Users) authentication.getPrincipal();
    }
}
