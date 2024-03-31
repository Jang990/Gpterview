package com.mock.interview.interviewquestion.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionForm {
    private Long category;
    private Long field;
    private List<String> tech;
    private QuestionTypeForView type;
    private String content;

    public void setTech(String tech) {
        if(tech == null || tech.isBlank())
            this.tech = new ArrayList<>();
        else
            this.tech = Arrays.stream(tech.split(","))
                    .map(String::trim).map(String::toUpperCase)
                    .filter(StringUtils::hasText).toList();
    }
}
