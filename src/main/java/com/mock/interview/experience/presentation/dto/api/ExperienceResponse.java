package com.mock.interview.experience.presentation.dto.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mock.interview.interview.infra.progress.dto.InterviewTopic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExperienceResponse implements InterviewTopic {
    private Long id;
    private String content;

    @Override
    @JsonIgnore
    public String getName() {
        return content;
    }
}
