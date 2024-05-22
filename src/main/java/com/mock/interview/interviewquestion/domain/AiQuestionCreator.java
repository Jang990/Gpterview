package com.mock.interview.interviewquestion.domain;

import com.mock.interview.interview.infra.cache.dto.InterviewInfo;
import com.mock.interview.interview.infra.progress.dto.InterviewProgress;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interviewquestion.infra.gpt.dto.MessageHistory;

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

    String create(InterviewInfo interviewInfo, InterviewProgress interviewProgress, MessageHistory history, CreationOption creationOption);
}
