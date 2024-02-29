package com.mock.interview.interview.presentation.dto.message;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuestionInInterviewDto {
    private Long conversationPairId;
    private Long questionId;
    private String role;

    @Size(min = 3, max = 200)
    private String content;
}
