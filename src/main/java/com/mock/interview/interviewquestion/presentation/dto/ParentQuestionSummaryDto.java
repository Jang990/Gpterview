package com.mock.interview.interviewquestion.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParentQuestionSummaryDto {
    private long id;
    private String content;
    private long ownerId;
    private boolean isHidden;
}
