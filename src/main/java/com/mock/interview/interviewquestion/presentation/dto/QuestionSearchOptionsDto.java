package com.mock.interview.interviewquestion.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Repository 전송용
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionSearchOptionsDto {
    private QuestionSearchCond searchCond;
    private Long parentQuestionIdCond;
    private String categoryNameCond;
    private String positionNameCond;
    private String createdByCond;
}
