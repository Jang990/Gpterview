package com.mock.interview.interview.infra.cache;

import com.mock.interview.interview.infra.cache.dto.InterviewInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InterviewRedisRepository {
    private final RedisTemplate<String, InterviewInfo> interviewRedisTemplate;
    private final String INTERVIEW_PROGRESS_KEY_PREFIX = "INTERVIEW:PROGRESS:%d";

    private String createKey(long interviewId) {
        return String.format(INTERVIEW_PROGRESS_KEY_PREFIX, interviewId);
    }

    public Optional<InterviewInfo> find(long interviewId) {
        String key = createKey(interviewId);
        return Optional.ofNullable(interviewRedisTemplate.opsForValue().get(key));
    }

    public void save(long interviewId, InterviewInfo data, long expiredMinute) {
        String key = createKey(interviewId);
        interviewRedisTemplate.opsForValue().set(key, data, Duration.ofMinutes(expiredMinute));
    }

    public void delete(long interviewId) {
        String key = createKey(interviewId);
        interviewRedisTemplate.delete(key);
    }


}
