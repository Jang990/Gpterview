package com.mock.interview.interviewquestion.infra.ai.prompt.configurator;

import com.mock.interview.interviewquestion.infra.ai.dto.InterviewProfile;
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
                promptTemplate, profile.department(), profile.field(),
                profile.skills().toString(), profile.experience().toString()
        );
    }
}
