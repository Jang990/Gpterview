package com.mock.interview.interviewconversationpair.event;

import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.interview.domain.InterviewContinuedEvent;
import com.mock.interview.interview.domain.exception.InterviewNotFoundException;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.infra.InterviewRepository;
import com.mock.interview.interviewconversationpair.domain.QuestionRecommendRule;
import com.mock.interview.interviewconversationpair.infra.InterviewConversationPairRepository;
import com.mock.interview.interviewquestion.infra.InterviewQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InterviewContinuedEventHandler {
    private final InterviewConversationPairRepository interviewConversationPairRepository;
    private final InterviewRepository interviewRepository;
    private final InterviewQuestionRepository interviewQuestionRepository;
    private final QuestionRecommendRule questionRecommendRule;

    @EventListener(InterviewContinuedEvent.class)
    public void handle(InterviewContinuedEvent event) {
        Interview interview = interviewRepository.findById(event.interviewId())
                .orElseThrow(InterviewNotFoundException::new);
        JobCategory department = interview.getAppliedJob().getDepartment();

        long questionCount = interviewQuestionRepository.countDepartmentQuestion(department.getName());
        questionRecommendRule.start(interviewConversationPairRepository, interview, questionCount);
    }
}
