package com.mock.interview.interview.application;

import com.mock.interview.category.domain.exception.JobCategoryNotFoundException;
import com.mock.interview.category.domain.model.JobPosition;
import com.mock.interview.category.infra.JobCategoryRepository;
import com.mock.interview.category.infra.JobPositionRepository;
import com.mock.interview.experience.application.helper.ExperienceFinder;
import com.mock.interview.experience.infra.ExperienceRepository;
import com.mock.interview.interview.application.dto.InterviewTopicDto;
import com.mock.interview.interview.domain.model.*;
import com.mock.interview.interview.presentation.dto.InterviewAccountForm;
import com.mock.interview.interview.presentation.dto.InterviewConfigForm;
import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.interview.infra.lock.creation.InterviewCreationUserLock;
import com.mock.interview.tech.application.helper.TechFinder;
import com.mock.interview.tech.infra.TechnicalSubjectsRepository;
import com.mock.interview.user.domain.exception.UserNotFoundException;
import com.mock.interview.user.domain.model.Users;
import com.mock.interview.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class InterviewService {
    private final UserRepository userRepository;
    private final TechnicalSubjectsRepository technicalSubjectsRepository;
    private final ExperienceRepository experienceRepository;
    private final InterviewStartService interviewStartService;
    private final JobCategoryRepository jobCategoryRepository;
    private final JobPositionRepository jobPositionRepository;

    private final CandidateInfoCreator candidateInfoCreator;
    private final InterviewTimerCreator timerCreator;

    @InterviewCreationUserLock
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public long createCustomInterview(long loginId, InterviewConfigForm interviewConfig, InterviewAccountForm accountForm) {
        CandidateInfo candidateInfo = loadCandidateInfo(loginId, accountForm.getCategoryId(), accountForm.getPositionId());
        InterviewTopicDto topics = loadInterviewTopics(loginId, interviewConfig, accountForm);
        InterviewTimer timer = timerCreator.create(interviewConfig.getDurationMinutes());

        return interviewStartService
                .start(candidateInfo, topics, timer)
                .getId();
    }

    private InterviewTopicDto loadInterviewTopics(
            long loginId,
            InterviewConfigForm interviewConfig,
            InterviewAccountForm accountForm) {
        return InterviewTopicDto.builder()
                .type(interviewConfig.getInterviewType())
                .techTopics(
                        TechFinder.findTechs(
                                technicalSubjectsRepository,
                                accountForm.getTechIds()
                        )
                )
                .experienceTopics(
                        ExperienceFinder.findUserExperiences(
                                experienceRepository,
                                accountForm.getExperienceIds(),
                                loginId
                        )
                )
                .build();
    }

    private CandidateInfo loadCandidateInfo(long loginId, Long categoryId, Long positionId) {
        Users users = userRepository.findForInterviewSetting(loginId)
                .orElseThrow(UserNotFoundException::new);
        JobCategory category = jobCategoryRepository.findById(categoryId)
                .orElseThrow(JobCategoryNotFoundException::new);
        JobPosition position = jobPositionRepository.findById(positionId)
                .orElseThrow(JobCategoryNotFoundException::new);

        return candidateInfoCreator.create(users, category, position);
    }
}
