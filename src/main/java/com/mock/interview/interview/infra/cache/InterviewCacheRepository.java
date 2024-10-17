package com.mock.interview.interview.infra.cache;

import com.mock.interview.category.application.helper.CategoryConvertor;
import com.mock.interview.experience.application.helper.ExperienceConvertor;
import com.mock.interview.global.TimeDifferenceCalculator;
import com.mock.interview.interview.domain.model.InterviewExperienceLink;
import com.mock.interview.interview.domain.model.InterviewTechLink;
import com.mock.interview.interview.infra.InterviewRepository;
import com.mock.interview.interview.infra.cache.dto.InterviewConfig;
import com.mock.interview.interview.infra.cache.dto.InterviewInfo;
import com.mock.interview.interview.infra.cache.dto.InterviewProfile;
import com.mock.interview.interview.domain.exception.InterviewNotFoundException;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.tech.application.helper.TechConvertHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

/**
 * AI에게 요청을 보낼 정보를 캐싱하고 가져올 때 쓸 것.
 */
@Service
@RequiredArgsConstructor
public class InterviewCacheRepository {

    private final InterviewRedisRepository redisRepository;
    private final InterviewRepository interviewRepository;

    public InterviewInfo findProgressingInterviewInfo(long interviewId) {
        Optional<InterviewInfo> cache = redisRepository.find(interviewId);
        if (cache.isPresent()) {
            return cache.get();
        }

        Interview interview = interviewRepository.findInterviewSetting(interviewId)
                .orElseThrow(InterviewNotFoundException::new);
        InterviewInfo result = convert(interview);

        long diffMinute = TimeDifferenceCalculator
                .calculate(ChronoUnit.MINUTES, LocalDateTime.now(), interview.getTimer().getExpiredAt());
        if (diffMinute > 0) {
            redisRepository.save(interviewId, result, diffMinute);
        }
        return result;
    }

    public void expireInterviewInfo(long interviewId) {
        redisRepository.delete(interviewId);
    }

    public InterviewInfo convert(Interview interview) {
        InterviewProfile profile = new InterviewProfile(
                CategoryConvertor.convert(interview.getCategory()),
                CategoryConvertor.convert(interview.getPosition()),
                TechConvertHelper.convertTopic(interview.getTechLink().stream().map(InterviewTechLink::getTechnicalSubjects).toList()),
                ExperienceConvertor.convertTopic(interview.getExperienceLink().stream().map(InterviewExperienceLink::getExperience).toList())
        );
        InterviewConfig interviewConfig = new InterviewConfig(interview.getType(), interview.getTimer().getStartedAt(), interview.getTimer().getExpiredAt());
        return new InterviewInfo(interview.getId(), profile, interviewConfig);
    }
}
