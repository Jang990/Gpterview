package com.mock.interview.interviewquestion.presentation.dto;

import com.mock.interview.experience.presentation.dto.ExperienceDto;
import com.mock.interview.interviewanswer.presentation.dto.AnswerDetailDto;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
public class QuestionDetailDto {
    @Setter(AccessLevel.PRIVATE)
    private QuestionOverview question;
    @Setter(AccessLevel.PRIVATE)
    private QuestionOverview parentQuestion;
    @Setter(AccessLevel.PRIVATE)
    private ExperienceDto experience;
    private List<AnswerDetailDto> answerTop3;
    private List<ChildQuestionOverview> childQuestionTop3;

    public QuestionDetailDto(QuestionOverview question, QuestionOverview parentQuestion, ExperienceDto experience) {
        this.question = question;
        this.parentQuestion = parentQuestion;
        this.experience = experience;
    }

    public void verifyExperience(long loginId) {
        if(!question.isHidden())
            return;
        if(question.getOwnerId().equals(loginId))
            return;
        experience = null;
    }
}
