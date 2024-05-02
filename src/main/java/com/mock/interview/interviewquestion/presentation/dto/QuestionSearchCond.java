package com.mock.interview.interviewquestion.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 변수 변경 시 question-list-nav와 동기화 필요 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionSearchCond {
    private String keywordCond;
    private QuestionTypeForView typeCond;
}
