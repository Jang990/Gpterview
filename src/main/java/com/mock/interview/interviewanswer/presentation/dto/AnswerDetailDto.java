package com.mock.interview.interviewanswer.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDetailDto {
    private Long id;
    private String createdBy;
    private LocalDateTime createdAt;
    private String content;
    private long likes;
}
