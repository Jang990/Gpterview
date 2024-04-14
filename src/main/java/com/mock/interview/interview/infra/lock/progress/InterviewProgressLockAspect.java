package com.mock.interview.interview.infra.lock.progress;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

/** @see InterviewProgressLock
 * 해당 어노테이션의 AOP 구현체
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class InterviewProgressLockAspect {
    private final StringRedisTemplate stringRedisTemplate;
    private final String KEY_FORMAT = "INTERVIEW:%d:USER:%d:PROGRESS";
    private final String LOCK_VALUE = "LOCK";
    private final long LOCK_TIMEOUT_MS = 2_000;

    @Around("@within(com.mock.interview.interview.infra.lock.progress.InterviewProgressLock) " +
            "|| @annotation(com.mock.interview.interview.infra.lock.progress.InterviewProgressLock)")
    public Object checkTime(ProceedingJoinPoint pjp) throws Throwable {
        InterviewProgressLockable lockInfo = getLockInfo(pjp.getArgs());
        String key = createKey(lockInfo);

        try {
            boolean lockAlreadyExists = !lock(key);
            if(lockAlreadyExists)
                // 여러 사용자가 몰리는 부분이 아님. 대기하지 않고 바로 예외를 반환함
                throw new AlreadyInterviewProgressingException();
            return pjp.proceed();
        } finally {
            release(key);
        }
    }

    private String createKey(InterviewProgressLockable lockInfo) {
        verify(lockInfo);
        return String.format(KEY_FORMAT, lockInfo.getInterviewId(), lockInfo.getUserId());
    }

    private void verify(InterviewProgressLockable lockInfo) {
        if (lockInfo == null) {
            log.error("InterviewProgressLockable null 불가능");
            throwServerException();
        }

        if (lockInfo.getInterviewId() == null || lockInfo.getInterviewId() == 0) {
            log.error("InterviewProgressLockable에 InterviewId 누락");
            throwServerException();
        }

        if (lockInfo.getUserId() == null || lockInfo.getUserId() == 0) {
            log.error("InterviewProgressLockable에 UserId 누락");
            throwServerException();
        }
    }

    private InterviewProgressLockable getLockInfo(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof InterviewProgressLockable dto) {
                return dto;
            }
        }

        log.error("InterviewProgressLock 점검 필요. Aspect를 적용한 곳에 InterviewProgressLockable를 받을 수 없음");
        throwServerException();
        return null;
    }

    private static void throwServerException() {
        throw new IllegalArgumentException("서버 오류");
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
