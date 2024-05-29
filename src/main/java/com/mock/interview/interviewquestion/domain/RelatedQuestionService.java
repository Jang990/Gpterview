package com.mock.interview.interviewquestion.domain;

import com.mock.interview.category.presentation.dto.response.CategoryResponse;
import com.mock.interview.interview.infra.progress.dto.InterviewProgress;
import com.mock.interview.interviewquestion.infra.RelatedQuestionRepository;
import com.mock.interview.interviewquestion.infra.recommend.dto.QuestionMetaData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RelatedQuestionService {
    private final int RECOMMENDED_QUESTION_SIZE = 30;

    public List<QuestionMetaData> findRelatedQuestions(
            RelatedQuestionRepository relatedQuestionRepository,
            CategoryResponse category, InterviewProgress interviewProgress,
            List<Long> appearedQuestionIds
    ) {
        final PageRequest pageable = PageRequest.of(0, RECOMMENDED_QUESTION_SIZE);
        return switch (interviewProgress.phase()) {
            case TECHNICAL -> relatedQuestionRepository.findTechQuestion(category.getId(), interviewProgress.getTopicId(), appearedQuestionIds, pageable);
            case EXPERIENCE -> relatedQuestionRepository.findExperienceQuestion(interviewProgress.getTopicId(), appearedQuestionIds, pageable);
            case PERSONAL -> relatedQuestionRepository.findPersonalQuestion(appearedQuestionIds, pageable);
        };
    }
}
