package com.mock.interview.interviewquestion.presentation.dto;

import com.mock.interview.experience.presentation.dto.ExperienceDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuestionDetailDto {
    private QuestionOverview question;
    private QuestionOverview parentQuestion;
    private ExperienceDto experience;
}
