package com.mock.interview.interview.application;

import com.mock.interview.interview.InterviewDomain;
import com.mock.interview.interview.domain.Interview;
import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.interview.presentation.dto.InterviewResponse;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import com.mock.interview.interview.domain.exception.InterviewNotFoundException;
import com.mock.interview.interview.infrastructure.InterviewRepository;
import com.mock.interview.conversation.infrastructure.interview.dto.InterviewConfig;
import com.mock.interview.conversation.infrastructure.interview.dto.InterviewInfo;
import com.mock.interview.conversation.infrastructure.interview.dto.InterviewProfile;
import com.mock.interview.candidate.domain.model.CandidateConfig;
import com.mock.interview.candidate.domain.exception.CandidateConfigNotFoundException;
import com.mock.interview.candidate.infrastructure.CandidateConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class InterviewService {

    private final InterviewRepository repository;
    private final CandidateConfigRepository profileRepository;
    private final InterviewDomain interviewDomain;

    public long create(long loginId,long candidateConfigId) {
        CandidateConfig candidateConfig = profileRepository.findInterviewConfig(candidateConfigId, loginId)
                .orElseThrow(CandidateConfigNotFoundException::new);
        Interview interview = Interview.startInterview(candidateConfig, candidateConfig.getUsers());
        return repository.save(interview).getId();
    }

    public InterviewResponse findActiveInterview(long userId) {
        // TODO: 면접이 expiredTime이 끝나면 종료된다는 것을 가정한 코드이다.
        //      - expiredTime이 끝나면 실제로 active가 false가 되도록 구현해야 한다.
        Interview activeInterview = repository.findActiveInterview(userId)
                .orElseThrow(InterviewNotFoundException::new);
        return convert(activeInterview);
    }

    private InterviewResponse convert(Interview activeInterview) {
        return new InterviewResponse(activeInterview.getId(), activeInterview.getTitle().getTitle());
    }

    @Transactional(readOnly = true)
    public InterviewInfo findInterviewForAIRequest(long loginId, long interviewId) {
        Interview interview = repository.findInterviewSetting(interviewId, loginId)
                .orElseThrow(InterviewNotFoundException::new);
        return convert(interview, interview.getCandidateConfig().getDepartment(), interview.getCandidateConfig().getAppliedJob());
    }

    private static InterviewInfo convert(Interview interview, JobCategory department, JobCategory field) {
        CandidateConfig profile = interview.getCandidateConfig();
        return new InterviewInfo(
                new InterviewProfile(
                        department.getName(), field.getName(),
                        profile.getTechSubjects().stream().map(TechnicalSubjects::getName).toList(),
                        List.of(profile.getExperience())
                ),
                new InterviewConfig(interview.getCandidateConfig().getType(), interview.getExpiredTime())
        );
    }
}
