package com.mock.interview.interview.infra;

import com.mock.interview.interviewquestion.infra.ai.dto.InterviewInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InterviewRedisRepository {
    private final RedisTemplate<String, InterviewInfo> interviewRedisTemplate;
    private final String INTERVIEW_PROGRESS_KEY_PREFIX = "INTERVIEW:PROGRESS:";

    private String createKey(long interviewId) {
        return INTERVIEW_PROGRESS_KEY_PREFIX + interviewId;
    }

    public Optional<InterviewInfo> findActiveInterview(long interviewId) {
        String key = createKey(interviewId);
        return Optional.ofNullable(interviewRedisTemplate.opsForValue().get(key));
    }

    public void saveInterviewIfActive(long interviewId, InterviewInfo data, long expiredMinute) {
        String key = createKey(interviewId);
        interviewRedisTemplate.opsForValue().set(key, data, Duration.ofMinutes(expiredMinute));
    }


}
