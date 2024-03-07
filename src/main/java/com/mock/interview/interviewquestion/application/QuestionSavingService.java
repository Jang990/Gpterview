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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionSavingService {
    private final InterviewQuestionRepository interviewQuestionRepository;
    private final JobCategoryRepository jobCategoryRepository;
    private final TechnicalSubjectsRepository technicalSubjectsRepository;
    public long save(QuestionForm form, List<TechnicalSubjectsResponse> relationalTech) {
        List<TechnicalSubjects> techList = technicalSubjectsRepository.findAllById(TechConvertHelper.convertToTechId(relationalTech));
        JobCategory detailCategory = findMoreDetailCategory(form.getDepartmentName(), form.getFieldName());

        InterviewQuestion question = InterviewQuestion.create(form.getContent(), form.getType(), detailCategory, techList);
        return interviewQuestionRepository.save(question).getId();
    }

    private JobCategory findMoreDetailCategory(String department, String field) {
        if(StringUtils.hasText(field))
            return jobCategoryRepository.findByName(field)
                .orElseThrow(JobCategoryNotFoundException::new);

        return jobCategoryRepository.findByName(department)
                .orElseThrow(JobCategoryNotFoundException::new);
    }
}
