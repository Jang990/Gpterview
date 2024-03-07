package com.mock.interview.interviewquestion.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionForm {
    private String departmentName;
    private String fieldName;
    private List<String> tech;
    private QuestionTypeForView type;
    private String content;
}
