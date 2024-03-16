package com.mock.interview.interviewquestion.application;

import com.mock.interview.category.domain.exception.JobCategoryNotFoundException;
import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.category.infrastructure.JobCategoryRepository;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interviewquestion.infra.InterviewQuestionRepository;
import com.mock.interview.interviewquestion.presentation.dto.QuestionForm;
import com.mock.interview.tech.application.TechConvertHelper;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import com.mock.interview.tech.infrastructure.TechnicalSubjectsRepository;
import com.mock.interview.tech.presentation.dto.TechnicalSubjectsResponse;
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
    private final TechnicalSubjectsRepository technicalSubjectsRepository;
    private final UserRepository userRepository;
    public long save(long loginId, QuestionForm form, List<TechnicalSubjectsResponse> relationalTech) {
        Users users = userRepository.findById(loginId).orElseThrow(UserNotFoundException::new);
        List<TechnicalSubjects> techList = technicalSubjectsRepository.findAllById(TechConvertHelper.convertToTechId(relationalTech));
        JobCategory detailCategory = findMoreDetailCategory(form.getDepartment(), form.getField());

        InterviewQuestion question = InterviewQuestion.create(
                interviewQuestionRepository, form.getContent(),
                form.getType(), detailCategory, techList, users
        );
        return question.getId();
    }

    private JobCategory findMoreDetailCategory(Long department, Long field) {
        if(field == null)
            return jobCategoryRepository.findById(department)
                .orElseThrow(JobCategoryNotFoundException::new);

        return jobCategoryRepository.findById(field)
                .orElseThrow(JobCategoryNotFoundException::new);
    }
}
