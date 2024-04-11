package com.mock.interview.interviewquestion.domain;

import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interviewquestion.infra.RecommendedQuestion;

public interface AiQuestionCreator {
    enum CreationOption {
        NORMAL, CHANGING_TOPIC
    }

    // TODO: pair 객체를 파라미터로 넘겨서 구현체에서 처리하는게 좋을까?
    static CreationOption selectCreationOption(InterviewConversationPair conversationPair) {
        if(conversationPair.isChangeTopicStatus())
            return CreationOption.CHANGING_TOPIC;
        else
            return CreationOption.NORMAL;
    }

    InterviewQuestion createQuestion(long interviewId, CreationOption creationOption);
    RecommendedQuestion create(long interviewId, CreationOption creationOption);
}
