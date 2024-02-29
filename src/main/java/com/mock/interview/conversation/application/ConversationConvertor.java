package com.mock.interview.conversation.application;

import com.mock.interview.conversation.domain.model.InterviewConversation;
import com.mock.interview.conversation.infrastructure.interview.dto.Message;
import com.mock.interview.conversation.presentation.dto.InterviewRole;
import com.mock.interview.interviewanswer.domain.model.InterviewAnswer;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;

public class ConversationConvertor {
    public static Message convert(InterviewConversation conversation) {
        return new Message(conversation.getInterviewConversationType().toString(), conversation.getContent());
    }

    public static Message convert(InterviewQuestion interviewQuestion) {
        return new Message(InterviewRole.AI.toString(), interviewQuestion.getQuestion());
    }

    public static Message convert(InterviewAnswer interviewAnswer) {
        if(interviewAnswer == null)
            return null;
        return new Message(InterviewRole.USER.toString(), interviewAnswer.getAnswer());
    }
}
