package com.mock.interview.interview.infra;

import com.mock.interview.experience.application.helper.ExperienceFinder;
import com.mock.interview.experience.infra.ExperienceRepository;
import com.mock.interview.global.RepositoryConst;
import com.mock.interview.interview.domain.exception.InterviewAlreadyInProgressException;
import com.mock.interview.experience.domain.Experience;
import com.mock.interview.interview.presentation.dto.InterviewAccountForm;
import com.mock.interview.interview.presentation.dto.InterviewConfigForm;
import com.mock.interview.interview.domain.InterviewStarter;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.tech.application.helper.TechFinder;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import com.mock.interview.tech.infra.TechnicalSubjectsRepository;
import com.mock.interview.user.domain.exception.DailyInterviewLimitExceededException;
import com.mock.interview.user.domain.model.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewStarterImpl implements InterviewStarter {
    private final InterviewRepository repository;
    private final ExperienceRepository experienceRepository;
    private final TechnicalSubjectsRepository technicalSubjectsRepository;

    private final int FIRST_IDX = 0;
    @Override
    public Interview start(Users users, InterviewConfigForm interviewConfig, InterviewAccountForm accountForm) {
        List<Interview> currentInterview = repository.findCurrentInterview(users.getId(), RepositoryConst.LIMIT_ONE);
        if (!currentInterview.isEmpty()) {
            verifyCurrentInterview(currentInterview.get(FIRST_IDX));
        }

        Interview interview = Interview.startInterview(interviewConfig, users, users.getCategory(), users.getPosition());
        switch (interviewConfig.getInterviewType()) {
            case TECHNICAL -> connectTech(interview, accountForm.getTechIds());
            case EXPERIENCE -> connectUsersExperience(interview, users, accountForm.getExperienceIds());
            case PERSONALITY -> { /* 딱히 할게 없다. */ }
            default -> throw new IllegalArgumentException("지원하지 않는 면접 타입");
        }

        return interview;
    }

    private void verifyCurrentInterview(Interview currentInterview) {
        if(currentInterview.isActive())
            throw new InterviewAlreadyInProgressException();
        if(currentInterview.isExecutedToday())
            throw new DailyInterviewLimitExceededException();
    }


    private void connectTech(Interview interview, List<Long> techIds) {
        List<TechnicalSubjects> techs = TechFinder.findTechs(technicalSubjectsRepository, techIds);
        if(techs.isEmpty())
            return;
        interview.linkTech(techs);
    }


    private void connectUsersExperience(Interview interview, Users users, List<Long> selectedExperience) {
        List<Experience> userExperiences = ExperienceFinder
                .findUserExperiences(experienceRepository, selectedExperience, users.getId());
        if(userExperiences.isEmpty())
            return;
        interview.linkExperience(userExperiences);
    }

}
