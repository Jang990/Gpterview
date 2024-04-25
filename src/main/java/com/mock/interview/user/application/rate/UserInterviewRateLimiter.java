package com.mock.interview.user.application.rate;

import com.mock.interview.user.domain.UsersConst;
import com.mock.interview.user.domain.exception.DailyInterviewLimitExceededException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class UserInterviewRateLimiter {
    private final UserInterviewRateLimitService rateLimitService;
    @Around("@within(com.mock.interview.user.application.rate.WithUserInterviewRateLimiter) " +
            "|| @annotation(com.mock.interview.user.application.rate.WithUserInterviewRateLimiter)")
    public Object limit(ProceedingJoinPoint pjp) throws Throwable {
        try {
            long result = rateLimitService.increase();
            if (result > UsersConst.DAILY_INTERVIEW_USAGE) {
                rateLimitService.decrease();
                throw new DailyInterviewLimitExceededException();
            }
            return pjp.proceed();
        } catch (Throwable throwable) {
            System.out.println(throwable);
            rateLimitService.decrease();
            throw throwable;
        }
    }
}
