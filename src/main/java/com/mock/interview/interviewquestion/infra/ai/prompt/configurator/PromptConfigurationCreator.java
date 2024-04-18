package com.mock.interview.interviewquestion.infra.ai.prompt.configurator;

import com.mock.interview.category.presentation.dto.response.CategoryResponse;
import com.mock.interview.experience.presentation.dto.ExperienceDto;
import com.mock.interview.interviewquestion.infra.cache.dto.InterviewProfile;
import com.mock.interview.interviewquestion.infra.ai.prompt.configurator.template.validator.PromptTemplateValidator;
import com.mock.interview.tech.presentation.dto.TechnicalSubjectsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PromptConfigurationCreator {
    private final PromptTemplateValidator templateValidator;

    public PromptConfiguration create(String promptTemplate, PromptConfigElements elements) {
        templateValidator.verify(promptTemplate, elements);
        return new PromptConfiguration(promptTemplate, elements);
    }
}
