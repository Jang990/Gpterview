package com.mock.interview.experience.presentation.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExperienceDeleteOptions {
    Boolean isDeleteRelatedQuestion;

    @JsonIgnore
    public boolean isDeleteRelatedQuestion() {
        return isDeleteRelatedQuestion != null && isDeleteRelatedQuestion;
    }
}
