package com.mock.interview.interviewquestion.domain;

import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.interviewquestion.infra.interview.dto.Message;
import com.mock.interview.interviewquestion.infra.interview.strategy.stage.InterviewProgress;
import com.mock.interview.interviewquestion.infra.interview.strategy.stage.InterviewStage;
import com.mock.interview.global.Events;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interviewquestion.event.CreatedCustomInterviewQuestionEvent;
import com.mock.interview.interviewquestion.infra.InterviewQuestionRepository;
import com.mock.interview.interviewquestion.infra.PublishedQuestionInfo;
import com.mock.interview.interviewquestion.presentation.dto.QuestionForm;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import com.mock.interview.user.domain.model.Users;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreationQuestionInCustomInterviewService {
    public InterviewQuestion save(
            InterviewQuestionRepository repository,
            Interview interview, Message message
    ) {
        PublishedQuestionInfo publishedQuestion = new PublishedQuestionInfo(
                "GPT", message.getContent(), new InterviewProgress(InterviewStage.TECHNICAL, 0.3)); // TODO: AI 서비스에서 가져올 것.
        InterviewQuestion question = InterviewQuestion.createInInterview(repository, interview.getUsers(), interview.getAppliedJob(), publishedQuestion);
        Events.raise(new CreatedCustomInterviewQuestionEvent(interview.getId(), question.getId()));
        return question;
    }
}
