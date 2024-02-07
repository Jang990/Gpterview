package com.mock.interview.interview.application;

import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.interview.infrastructure.lock.creation.InterviewUserLock;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class InterviewService {

    private final InterviewRepository repository;
    private final CandidateConfigRepository profileRepository;

    @InterviewUserLock
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public long create(long loginId,long candidateConfigId) {
        CandidateConfig candidateConfig = profileRepository.findInterviewConfig(candidateConfigId, loginId)
                .orElseThrow(CandidateConfigNotFoundException::new);
        Interview interview = Interview.startInterview(repository, candidateConfig, candidateConfig.getUsers());
        return repository.save(interview).getId();
    }

    @Transactional(readOnly = true)
    public Optional<InterviewResponse> findActiveInterview(long userId) {
        Optional<Interview> optionalInterview = repository.findActiveInterview(userId);
        if(optionalInterview.isEmpty())
            return Optional.empty();

        return Optional.of(convert(optionalInterview.get()));
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
                        profile.getExperience()
                ),
                new InterviewConfig(interview.getCandidateConfig().getType(), interview.getCreatedAt(), interview.getExpiredTime())
        );
    }
}
