package com.mock.interview.interview.presentation.dto.message;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterviewAnswerRequest {
    private Long id;

    @Size(min = 3, max = 200)
    private String content;
}
