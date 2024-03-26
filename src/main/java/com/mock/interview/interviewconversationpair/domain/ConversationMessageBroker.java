package com.mock.interview.interviewconversationpair.domain;

import com.mock.interview.interview.presentation.dto.InterviewRole;
import com.mock.interview.interview.presentation.dto.message.MessageDto;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;

import java.util.List;

public interface ConversationMessageBroker {
    void publish(long interviewId, long conversationPairId, List<MessageDto> messageList);

    static MessageDto createMessageDtoToPublish(InterviewQuestion question) {
        return new MessageDto(question.getId(), InterviewRole.AI, question.getQuestion());
    }
}
