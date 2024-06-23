package com.mock.interview.questionlike.infra.lock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

/** @see QuestionLikeLock
 * 해당 어노테이션의 AOP 구현체
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class QuestionLikeLockAspect {
    private final StringRedisTemplate stringRedisTemplate;
    private final String KEY_FORMAT = "QUESTION:%d:LIKE";
    private final String LOCK_VALUE = "LOCK";
    private final long LOCK_TIMEOUT_MS = 30_000;

    @Around("@within(com.mock.interview.questionlike.infra.lock.QuestionLikeLock) " +
            "|| @annotation(com.mock.interview.questionlike.infra.lock.QuestionLikeLock)")
    public Object applyLocking(ProceedingJoinPoint pjp) throws Throwable {
        QuestionLikeLockable lockInfo = getLockInfo(pjp.getArgs());
        String key = createKey(lockInfo);

        try {
            while (!lock(key)) {
                Thread.sleep(100);
            }
            return pjp.proceed();
        } finally {
            release(key);
        }
    }

    private String createKey(QuestionLikeLockable lockInfo) {
        verify(lockInfo);
        return String.format(KEY_FORMAT, lockInfo.getQuestionId());
    }

    private void verify(QuestionLikeLockable lockInfo) {
        if (lockInfo == null) {
            throwServerException("QuestionLikeLockable null 불가능");
        }

        if (lockInfo.getQuestionId() == 0) {
            throwServerException("QuestionLikeLockable에 QuestionId 누락");
        }
    }

    private QuestionLikeLockable getLockInfo(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof QuestionLikeLockable dto) {
                return dto;
            }
        }

        throwServerException("QuestionLikeLockAspect 점검 필요. Aspect를 적용한 곳에 QuestionLikeLockable를 받을 수 없음");
        return null;
    }

    private static void throwServerException(String logStr) {
        log.error(logStr);
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
