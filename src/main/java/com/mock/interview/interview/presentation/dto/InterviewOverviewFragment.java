package com.mock.interview.interview.presentation.dto;

import com.mock.interview.candidate.presentation.dto.InterviewType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class InterviewOverviewFragment {
    private long id;
    private String title;
    private int durationMinutes;
    private LocalDateTime createdAt;
}
