package com.mock.interview.interviewconversationpair.presentation.dto;

import com.mock.interview.conversation.presentation.dto.MessageDto;
import lombok.Data;

@Data
public class InterviewConversationPairDto {
    private MessageDto question;
    private MessageDto answer;

    public InterviewConversationPairDto(MessageDto question, MessageDto answer) {
        this.question = question;
        this.answer = answer;
    }
}
