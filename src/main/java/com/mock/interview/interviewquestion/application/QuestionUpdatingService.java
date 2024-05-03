package com.mock.interview.interviewquestion.application;

import com.mock.interview.category.application.helper.JobConnectionHelper;
import com.mock.interview.category.infra.JobCategoryRepository;
import com.mock.interview.category.infra.JobPositionRepository;
import com.mock.interview.experience.domain.Experience;
import com.mock.interview.experience.domain.exception.ExperienceNotFoundException;
import com.mock.interview.experience.infra.ExperienceRepository;
import com.mock.interview.interviewquestion.infra.QuestionTechLinkRepository;
import com.mock.interview.interviewquestion.application.helper.QuestionConvertor;
import com.mock.interview.interviewquestion.domain.exception.InterviewQuestionNotFoundException;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interviewquestion.infra.InterviewQuestionRepository;
import com.mock.interview.interviewquestion.presentation.dto.QuestionForm;
import com.mock.interview.tech.application.helper.TechConnectHelper;
import com.mock.interview.tech.infra.TechnicalSubjectsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionUpdatingService {
    private final InterviewQuestionRepository interviewQuestionRepository;
    private final JobCategoryRepository jobCategoryRepository;
    private final JobPositionRepository jobPositionRepository;
    private final TechnicalSubjectsRepository technicalSubjectsRepository;
    private final QuestionTechLinkRepository questionTechLinkRepository;
    private final ExperienceRepository experienceRepository;

    public void update(long loginId, long questionId, QuestionForm form) {
        InterviewQuestion question = interviewQuestionRepository.findUserQuestionWithAll(loginId, questionId)
                .orElseThrow(InterviewQuestionNotFoundException::new);
        question.changeQuestion(form.getContent());
        question.changeType(QuestionConvertor.convert(form.getQuestionType()));
        if (form.getExperienceId() != null) {
            Experience experience = experienceRepository.findByIdAndUserId(form.getExperienceId(), loginId)
                    .orElseThrow(ExperienceNotFoundException::new);
            question.changeExperience(experience);
        }
        if (form.getExperienceId() == null && question.getExperience() != null) {
            question.removeExperience();
        }

        JobConnectionHelper.connect(
                jobCategoryRepository, jobPositionRepository,
                question, form.getCategoryId(), form.getPositionId()
        );

        TechConnectHelper.replaceQuestionTech(
                technicalSubjectsRepository,
                questionTechLinkRepository,
                question, form.getTechIds()
        );
    }

    public void selectParentQuestion(long childQuestionId, long loginId, long parentQuestionId) {
        InterviewQuestion childQuestion = interviewQuestionRepository.findUserQuestion(loginId, childQuestionId)
                .orElseThrow(InterviewQuestionNotFoundException::new);
        InterviewQuestion parentQuestion = interviewQuestionRepository.findById(parentQuestionId)
                .orElseThrow(InterviewQuestionNotFoundException::new);
        childQuestion.linkParent(parentQuestion);
    }

}
