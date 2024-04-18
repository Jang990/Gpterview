package com.mock.interview.tech.presentation.dto;

import com.mock.interview.interview.infra.progress.dto.InterviewTopic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TechnicalSubjectsResponse implements InterviewTopic {
    private Long id;
    private String name;
}
