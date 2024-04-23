package com.mock.interview.interview.infra.prompt.configurator;

import com.mock.interview.interview.infra.prompt.configurator.template.validator.PromptTemplateValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PromptConfigurationCreator {
    private final PromptTemplateValidator templateValidator;

    public PromptConfiguration create(String promptTemplate, PromptElements elements) {
        if(elements.category() == null || elements.category().isBlank()
                || elements.field() == null || elements.field().isBlank())
            throw new IllegalArgumentException("CATEGORY, FIELD 필수");

        templateValidator.verify(promptTemplate, elements);
        return new PromptConfiguration(promptTemplate, elements);
    }
}
