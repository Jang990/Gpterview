package com.mock.interview.interview.infrastructure;

import com.mock.interview.candidate.domain.model.CandidateConfig;
import com.mock.interview.conversation.infrastructure.interview.dto.InterviewConfig;
import com.mock.interview.conversation.infrastructure.interview.dto.InterviewInfo;
import com.mock.interview.conversation.infrastructure.interview.dto.InterviewProfile;
import com.mock.interview.interview.domain.exception.InterviewNotFoundException;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * AI에게 요청을 보낼 정보를 캐싱하고 가져올 때 쓸 것.
 */
@Service
@RequiredArgsConstructor
public class InterviewCacheForAiRequest {

    private final RedisTemplate<String, InterviewInfo> interviewRedisTemplate;
    private final InterviewRepository interviewRepository;

    private final String INTERVIEW_PROGRESS_KEY_PREFIX = "INTERVIEW:PROGRESS:";

    public InterviewInfo findAiInterviewSetting(long interviewId) {
        String key = INTERVIEW_PROGRESS_KEY_PREFIX + interviewId;
        InterviewInfo result = interviewRedisTemplate.opsForValue().get(key);
        if (result != null) {
            return result;
        }

        Interview interview = interviewRepository.findInterviewSetting(interviewId)
                .orElseThrow(InterviewNotFoundException::new);
        result = convert(interview);

        long diffMinute = calculateMinuteDifference(interview.getExpiredTime());
        if(diffMinute > 0)
            interviewRedisTemplate.opsForValue().set(key, result, Duration.ofMinutes(diffMinute));

        return result;
    }

    private long calculateMinuteDifference(LocalDateTime expiredTime) {
        return ChronoUnit.MINUTES.between(LocalDateTime.now(), expiredTime);
    }

    public InterviewInfo convert(Interview interview) {
        CandidateConfig config = interview.getCandidateConfig();
        InterviewProfile profile = new InterviewProfile(
                config.getDepartment().getName(),
                config.getAppliedJob().getName(),
                config.getTechSubjects().stream().map(TechnicalSubjects::getName).toList(),
                List.of(config.getExperience())
        );
        InterviewConfig interviewConfig = new InterviewConfig(config.getType(), interview.getExpiredTime());
        return new InterviewInfo(profile, interviewConfig);
    }

}
