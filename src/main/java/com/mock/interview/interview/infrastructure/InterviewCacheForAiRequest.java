package com.mock.interview.interview.infrastructure;

import com.mock.interview.candidate.domain.model.CandidateConfig;
import com.mock.interview.conversation.infrastructure.interview.dto.InterviewConfig;
import com.mock.interview.conversation.infrastructure.interview.dto.InterviewInfo;
import com.mock.interview.conversation.infrastructure.interview.dto.InterviewProfile;
import com.mock.interview.interview.domain.exception.InterviewNotFoundException;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

/**
 * AI에게 요청을 보낼 정보를 캐싱하고 가져올 때 쓸 것.
 */
@Service
@RequiredArgsConstructor
public class InterviewCacheForAiRequest {

    private final InterviewRedisRepository redisRepository;
    private final InterviewRepository interviewRepository;

    public InterviewInfo findAiInterviewSetting(long interviewId) {
        Optional<InterviewInfo> cache = redisRepository.findActiveInterview(interviewId);
        if (cache.isPresent()) {
            return cache.get();
        }

        Interview interview = interviewRepository.findInterviewSetting(interviewId)
                .orElseThrow(InterviewNotFoundException::new);
        InterviewInfo result = convert(interview);

        redisRepository.saveInterviewIfActive(interviewId, result);
        return result;
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
