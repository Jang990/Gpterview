package com.mock.interview.interview.infra.lock.response;

import com.mock.interview.interviewquestion.infra.gpt.AISpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import com.mock.interview.interview.infra.lock.progress.InterviewProgressLockAspect;

import java.time.Duration;

/**
 * 사용되지 않음.
 * 사용자 요청을 처리하는 서비스에 락을 걸어서 중복 요청을 제어하도록 변경함
 * @see InterviewProgressLockAspect
 */
@Deprecated
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AiResponseAwaitLockAspect {
    private final StringRedisTemplate stringRedisTemplate;
    private final String KEY_FORMAT = "INTERVIEW:%d:AI:RESPONSE";
    private final String LOCK_VALUE = "LOCK";

    private final long EXTRA_PROCESSING_MS = 500;
    private final long LOCK_TIMEOUT_MS = AISpecification.CONNECT_TIMEOUT_MS
            + AISpecification.READ_TIMEOUT_MS
            + EXTRA_PROCESSING_MS;

    @Around("@within(com.mock.interview.interview.infra.lock.response.AiResponseAwaitLock) " +
            "|| @annotation(com.mock.interview.interview.infra.lock.response.AiResponseAwaitLock)")
    public Object checkTime(ProceedingJoinPoint pjp) throws Throwable {
        Long interviewId = getInterviewId(pjp.getArgs());
        if (interviewId == null)
            throw new IllegalArgumentException();

        String key = String.format(KEY_FORMAT, interviewId);
        try {
            boolean lockAlreadyExists = !lock(key);
            if(lockAlreadyExists)
                // 여러 사용자가 몰리는 부분이 아님. 대기하지 않고 바로 예외를 반환함
                throw new AlreadyAiResponseProcessingException();
            return pjp.proceed();
        } finally {
            release(key);
        }
    }

    private Long getInterviewId(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof LockableInterviewEvent event) {
                return event.getInterviewId();
            }
        }

        return null;
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
