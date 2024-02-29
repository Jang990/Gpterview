package com.mock.interview.interviewquestion.domain;

import com.mock.interview.interviewquestion.infra.interview.dto.Message;
import com.mock.interview.interviewquestion.infra.interview.strategy.stage.InterviewProgress;
import com.mock.interview.interviewquestion.infra.interview.strategy.stage.InterviewStage;
import com.mock.interview.global.Events;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interviewquestion.event.CreatedRunningInterviewQuestionEvent;
import com.mock.interview.interviewquestion.infra.InterviewQuestionRepository;
import com.mock.interview.interviewquestion.infra.PublishedQuestionInfo;
import org.springframework.stereotype.Service;

@Service
public class CreationQuestionInRunningInterviewService {
    public InterviewQuestion save(
            InterviewQuestionRepository repository,
            Interview interview, Message message
    ) {
        PublishedQuestionInfo publishedQuestion = new PublishedQuestionInfo(
                "GPT", message.getContent(), new InterviewProgress(InterviewStage.TECHNICAL, 0.3)); // TODO: AI 서비스에서 가져올 것.
        InterviewQuestion question = InterviewQuestion.createInInterview(interview.getUsers(), interview.getAppliedJob(), publishedQuestion);
        repository.save(question);
        Events.raise(new CreatedRunningInterviewQuestionEvent(interview.getId(), question.getId()));
        return question;
    }
}
