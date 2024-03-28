package com.mock.interview.interviewquestion.infra.cache;

import com.mock.interview.interviewquestion.domain.RecommendationTarget;
import com.mock.interview.interviewquestion.domain.Top3Question;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RecommendedQuestionCacheRepository {
    private final RedisTemplate<String, Top3Question> questionRedisTemplate;
    private final String RECOMMENDED_QUESTION_KEY_PREFIX = "INTERVIEW:{%d}:PAIR:{%d}:RECOMMENDED";
    private final long EXPIRED_MINUTE = 1;

    public Top3Question find(RecommendationTarget target) {
        String key = createCacheKey(target);
        Top3Question cache = questionRedisTemplate.opsForValue().get(key);
        if(cache == null)
            return new Top3Question(Collections.emptyList());
        return cache;
    }

    public void save(RecommendationTarget target, Top3Question cache) {
        String key = createCacheKey(target);
        questionRedisTemplate.opsForValue().set(key, cache, Duration.ofMinutes(EXPIRED_MINUTE));
    }

    private String createCacheKey(RecommendationTarget target) {
        return String.format(RECOMMENDED_QUESTION_KEY_PREFIX, target.interviewId(), target.pairId());
    }

}
