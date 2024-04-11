package com.mock.interview.interviewquestion.domain;

import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;

public interface AiQuestionCreator {
    enum CreationOption {
        NORMAL, CHANGING_TOPIC
    }
    
    static CreationOption selectCreationOption(InterviewConversationPair conversationPair) {
        if(conversationPair.isChangeTopicStatus())
            return CreationOption.CHANGING_TOPIC;
        else
            return CreationOption.NORMAL;
    }

    InterviewQuestion create(long interviewId, CreationOption creationOption);
}
