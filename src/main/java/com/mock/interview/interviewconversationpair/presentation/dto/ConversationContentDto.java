package com.mock.interview.interviewconversationpair.presentation.dto;

import com.mock.interview.interview.presentation.dto.message.MessageDto;
import lombok.Data;

@Data
public class ConversationContentDto {
    private InterviewConversationPairDto pair;
    private MessageDto question;
    private MessageDto answer;

    public ConversationContentDto(InterviewConversationPairDto pair, MessageDto question, MessageDto answer) {
        this.pair = pair;
        this.question = question;
        this.answer = answer;
    }
}
