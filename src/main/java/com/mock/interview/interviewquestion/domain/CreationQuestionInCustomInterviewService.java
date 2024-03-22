package com.mock.interview.interviewquestion.domain;

import com.mock.interview.interviewquestion.infra.ai.dto.Message;
import com.mock.interview.interviewquestion.infra.ai.progress.InterviewProgress;
import com.mock.interview.interviewquestion.infra.ai.progress.InterviewStage;
import com.mock.interview.global.Events;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interviewquestion.event.CreatedCustomInterviewQuestionEvent;
import com.mock.interview.interviewquestion.infra.InterviewQuestionRepository;
import com.mock.interview.interviewquestion.infra.PublishedQuestion;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CreationQuestionInCustomInterviewService {
    public InterviewQuestion save(
            InterviewQuestionRepository repository,
            Interview interview, PublishedQuestion publishedQuestion
    ) {
        InterviewQuestion question = InterviewQuestion.createInInterview(repository, interview.getUsers(), interview.getAppliedJob(), publishedQuestion);
        Events.raise(new CreatedCustomInterviewQuestionEvent(interview.getId(), question.getId()));
        return question;
    }
}
