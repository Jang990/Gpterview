package com.mock.interview.interviewquestion.domain;

import com.mock.interview.global.Events;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewquestion.domain.event.ConversationQuestionCreatedEvent;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import org.springframework.stereotype.Service;

@Service
public class AiConversationQuestionService {
    public void service(AiQuestionCreator aiQuestionCreator, InterviewConversationPair pair) {
        InterviewQuestion question = aiQuestionCreator
                .create(pair.getInterview().getId(), AiQuestionCreator.selectCreationOption(pair));

        Events.raise(new ConversationQuestionCreatedEvent(pair.getId(), question.getId()));
    }
}
