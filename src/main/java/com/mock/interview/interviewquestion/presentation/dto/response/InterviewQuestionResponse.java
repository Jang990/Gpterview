package com.mock.interview.interviewquestion.presentation.dto.response;

import com.mock.interview.interview.presentation.dto.InterviewRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterviewQuestionResponse {
    private Long id;
    private InterviewRole role;
    private String content;

    public static InterviewQuestionResponse createQuestion(long id, String content) {
        return new InterviewQuestionResponse(id, InterviewRole.AI, content);
    }
}
