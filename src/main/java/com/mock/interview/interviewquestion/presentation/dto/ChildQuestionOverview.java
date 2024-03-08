package com.mock.interview.interviewquestion.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ChildQuestionOverview {
    private Long id;
    private String createdBy;
    private LocalDateTime createdAt;
    private String content;
    private long likes;
}
