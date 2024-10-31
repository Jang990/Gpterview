package com.mock.interview.interview.application;

import com.mock.interview.category.domain.exception.JobCategoryNotFoundException;
import com.mock.interview.category.domain.model.JobPosition;
import com.mock.interview.category.infra.JobCategoryRepository;
import com.mock.interview.category.infra.JobPositionRepository;
import com.mock.interview.experience.application.helper.ExperienceFinder;
import com.mock.interview.experience.domain.Experience;
import com.mock.interview.experience.infra.ExperienceRepository;
import com.mock.interview.interview.domain.InterviewStartService;
import com.mock.interview.interview.domain.InterviewTimeHolder;
import com.mock.interview.interview.presentation.dto.InterviewAccountForm;
import com.mock.interview.interview.presentation.dto.InterviewConfigForm;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.interview.infra.lock.creation.InterviewCreationUserLock;
import com.mock.interview.interview.presentation.dto.InterviewType;
import com.mock.interview.tech.application.helper.TechFinder;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import com.mock.interview.interview.infra.InterviewRepository;
import com.mock.interview.tech.infra.TechnicalSubjectsRepository;
import com.mock.interview.user.domain.exception.UserNotFoundException;
import com.mock.interview.user.domain.model.Users;
import com.mock.interview.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class InterviewService {
    private final InterviewRepository repository;
    private final UserRepository userRepository;
    private final TechnicalSubjectsRepository technicalSubjectsRepository;
    private final ExperienceRepository experienceRepository;
    private final InterviewStartService interviewStartService;
    private final JobCategoryRepository jobCategoryRepository;
    private final JobPositionRepository jobPositionRepository;
    private final InterviewTimeHolder interviewTimeHolder;


    @InterviewCreationUserLock
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public long createCustomInterview(long loginId, InterviewConfigForm interviewConfig, InterviewAccountForm accountForm) {
        Users users = userRepository.findForInterviewSetting(loginId)
                .orElseThrow(UserNotFoundException::new);
        JobCategory category = jobCategoryRepository.findById(accountForm.getCategoryId())
                .orElseThrow(JobCategoryNotFoundException::new);
        JobPosition position = jobPositionRepository.findById(accountForm.getPositionId())
                .orElseThrow(JobCategoryNotFoundException::new);

        Interview interview = Interview.create(interviewTimeHolder, interviewConfig, users, category, position);
        List<Long> techIds = accountForm.getTechIds();
        if (interview.getType() == InterviewType.TECHNICAL && !techIds.isEmpty()) {
            connectTech(interview, techIds);
        }
        List<Long> experienceIds = accountForm.getExperienceIds();
        if (interview.getType() == InterviewType.EXPERIENCE && !experienceIds.isEmpty()) {
            connectUserExperience(interview, loginId, experienceIds);
        }

        interviewStartService.start(interview, repository, users);
        return interview.getId();
    }

    private void connectTech(Interview interview, List<Long> techIds) {
        List<TechnicalSubjects> techs = TechFinder.findTechs(technicalSubjectsRepository, techIds);
        interview.linkTech(techs);
    }

    private void connectUserExperience(Interview interview, long loginId, List<Long> experienceIds) {
        List<Experience> userExperiences = ExperienceFinder
                .findUserExperiences(experienceRepository, experienceIds, loginId);
        interview.linkExperience(userExperiences);
    }
}
