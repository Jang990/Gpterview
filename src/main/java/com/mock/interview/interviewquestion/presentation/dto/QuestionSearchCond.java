package com.mock.interview.interviewquestion.presentation.dto;

import com.mock.interview.tech.presentation.dto.TechViewDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionSearchCond {
    private String keywordCond;
    private QuestionTypeForView typeCond;
    private List<TechViewDto> techCond;

    public void setTechCond(String techString) {
        this.techCond = TechViewDto.convert(techString);
    }
}
