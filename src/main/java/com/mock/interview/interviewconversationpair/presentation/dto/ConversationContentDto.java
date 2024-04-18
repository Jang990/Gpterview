package com.mock.interview.interviewconversationpair.presentation.dto;

import com.mock.interview.interviewanswer.presentation.dto.InterviewAnswerRequest;
import com.mock.interview.interviewquestion.presentation.dto.response.InterviewQuestionResponse;
import lombok.Data;

@Data
public class ConversationContentDto {
    private InterviewConversationPairDto pair;
    private InterviewQuestionResponse question;
    private InterviewAnswerRequest answer;

    public ConversationContentDto(InterviewConversationPairDto pair, InterviewQuestionResponse question, InterviewAnswerRequest answer) {
        this.pair = pair;
        this.question = question;
        this.answer = answer;
    }
}
