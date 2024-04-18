package com.mock.interview.interviewquestion.infra.ai.prompt.configurator;

import com.mock.interview.interviewquestion.infra.ai.prompt.configurator.template.validator.PromptTemplateValidator;
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
