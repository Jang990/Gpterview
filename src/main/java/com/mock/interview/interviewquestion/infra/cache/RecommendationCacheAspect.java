package com.mock.interview.interviewquestion.infra.cache;

import com.mock.interview.interviewquestion.domain.RecommendationTarget;
import com.mock.interview.interviewquestion.domain.Top3Question;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 다음 클래스를 캐싱하는 클래스
 * @see com.mock.interview.interviewquestion.infra.QuestionRecommenderImpl
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RecommendationCacheAspect {
    private final RecommendedQuestionCacheRepository cacheRepository;

    @Around("execution(* com.mock.interview.interviewquestion.infra.QuestionRecommenderImpl.recommendTop3(..))")
    public Object checkTime(ProceedingJoinPoint pjp) throws Throwable {
        RecommendationTarget target = getParameter(pjp.getArgs());
        if (target == null)
            throw new IllegalArgumentException();

        Top3Question cache = cacheRepository.find(target);
        if (hasCache(cache)) {
            return cache;
        }

        Object result = pjp.proceed();
        if (result instanceof Top3Question top3Question) {
            cacheRepository.save(target, top3Question);
            return result;
        }

        throw new IllegalArgumentException();
    }

    private boolean hasCache(Top3Question cache) {
        return cache != null && cache.questions() != null && !cache.questions().isEmpty();
    }

    private RecommendationTarget getParameter(Object[] args) {
        for (Object arg : args) {
            if(arg instanceof RecommendationTarget target)
                return target;
        }
        throw new IllegalArgumentException();
    }
}
