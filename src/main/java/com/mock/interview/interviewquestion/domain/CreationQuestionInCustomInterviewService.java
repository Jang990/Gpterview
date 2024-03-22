package com.mock.interview.interviewquestion.domain;

import com.mock.interview.global.Events;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interviewquestion.event.CreatedCustomInterviewQuestionEvent;
import com.mock.interview.interviewquestion.infra.InterviewQuestionRepository;
import com.mock.interview.interviewquestion.infra.RecommendedQuestion;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreationQuestionInCustomInterviewService {
    public InterviewQuestion save(
            InterviewQuestionRepository repository,
            Interview interview, RecommendedQuestion recommendedQuestion,
            List<TechnicalSubjects> techList) {
        InterviewQuestion question = InterviewQuestion.createInInterview(
                repository, interview.getUsers(),
                interview.getAppliedJob(), recommendedQuestion, techList
        );
        Events.raise(new CreatedCustomInterviewQuestionEvent(interview.getId(), question.getId()));
        return question;
    }
}
