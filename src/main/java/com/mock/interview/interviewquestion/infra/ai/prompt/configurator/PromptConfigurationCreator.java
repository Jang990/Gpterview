package com.mock.interview.interviewquestion.infra.ai.prompt.configurator;

import com.mock.interview.interviewquestion.infra.cache.dto.InterviewProfile;
import com.mock.interview.interviewquestion.infra.ai.prompt.configurator.template.validator.PromptTemplateValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PromptConfigurationCreator {
    private final PromptTemplateValidator templateValidator;

    public PromptConfiguration create(String promptTemplate, InterviewProfile profile) {
        templateValidator.verify(promptTemplate, profile);
        return new PromptConfiguration(
                promptTemplate, profile.category(), profile.field(),
                profile.skills(), profile.experience()
        );
    }
}
