package com.mock.interview.interviewquestion.application;

import com.mock.interview.category.application.helper.JobConnectionHelper;
import com.mock.interview.category.infra.JobCategoryRepository;
import com.mock.interview.category.infra.JobPositionRepository;
import com.mock.interview.interviewquestion.application.helper.QuestionConvertor;
import com.mock.interview.interviewquestion.domain.exception.InterviewQuestionNotFoundException;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interviewquestion.infra.InterviewQuestionRepository;
import com.mock.interview.interviewquestion.presentation.dto.QuestionForm;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import com.mock.interview.tech.infra.TechnicalSubjectsRepository;
import com.mock.interview.user.domain.exception.UserNotFoundException;
import com.mock.interview.user.domain.model.Users;
import com.mock.interview.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionSavingService {
    private final InterviewQuestionRepository interviewQuestionRepository;
    private final JobCategoryRepository jobCategoryRepository;
    private final JobPositionRepository jobPositionRepository;
    private final TechnicalSubjectsRepository technicalSubjectsRepository;
    private final UserRepository userRepository;
    public long save(long loginId, QuestionForm form) {
        InterviewQuestion question = saveQuestion(loginId, form);
        return question.getId();
    }

    public long save(Long loginId, long parentQuestionId, QuestionForm form) {
        InterviewQuestion parentQuestion = interviewQuestionRepository.findById(parentQuestionId)
                .orElseThrow(InterviewQuestionNotFoundException::new);
        InterviewQuestion question = saveQuestion(loginId, form);
        question.linkParent(parentQuestion);
        return question.getId();
    }

    private InterviewQuestion saveQuestion(long loginId, QuestionForm form) {
        Users users = userRepository.findById(loginId).orElseThrow(UserNotFoundException::new);
        List<TechnicalSubjects> techList = technicalSubjectsRepository.findAllById(form.getTechIds());
        InterviewQuestion question = InterviewQuestion.create(
                interviewQuestionRepository, form.getContent(), users,
                QuestionConvertor.convert(form.getQuestionType()), users.getUsername()
        );
        JobConnectionHelper.connect(jobCategoryRepository, jobPositionRepository, question, form.getCategoryId(), form.getPositionId());
        question.linkTech(techList);
        return question;
    }
}
