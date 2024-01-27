package com.mock.interview.conversation.infrastructure.lock;

import com.mock.interview.conversation.infrastructure.interview.gpt.AISpecification;
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
public class AiResponseProcessingLockAspect {
    private final StringRedisTemplate stringRedisTemplate;
    private final String KEY_FORMAT = "INTERVIEW:%d:AI:RESPONSE";
    private final String LOCK_VALUE = "LOCK";

    private final long EXTRA_PROCESSING_MS = 500;
    private final long LOCK_TIMEOUT_MS = AISpecification.CONNECT_TIMEOUT_MS
            + AISpecification.READ_TIMEOUT_MS
            + EXTRA_PROCESSING_MS;

    @Around("@within(com.mock.interview.conversation.infrastructure.lock.InterviewUserLock) " +
            "|| @annotation(com.mock.interview.conversation.infrastructure.lock.InterviewUserLock)")
    public Object checkTime(ProceedingJoinPoint pjp) throws Throwable {
        Users authentication = getAuthenticatedUser();
        String key = createKey(authentication);
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
