package com.mock.interview.interview.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterviewOverview {
    private long id;
    private String title;
    private InterviewType type;
    private int durationMinutes;
    private LocalDateTime createdAt;
}
