package com.mock.interview.interview.infra.prompt.configurator.template.validator;


import com.mock.interview.interview.infra.prompt.configurator.PromptConfigElements;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @see com.mock.interview.interview.infra.prompt.fomatter.StringFormatter
 * StringFormatter를 통해 Prompt를 만들 때 템플릿 필수 정보를 설정하는 인터페이스
 */
@Component
@RequiredArgsConstructor
public class PromptTemplateValidator {
    private final TemplateFormatGetter formatGetter;

    public void verify(String rawTemplate, PromptConfigElements elements) {
        if(hasError(rawTemplate, elements))
            throw new InvalidConfigurationException();
    }

    private boolean hasError(String rawTemplate, PromptConfigElements elements) {
        return hasTemplateValueError(rawTemplate, formatGetter.getCategoryFormat(), elements.category())
                || hasTemplateValueError(rawTemplate, formatGetter.getFieldFormat(), elements.field())
                || hasTemplateValueError(rawTemplate, formatGetter.getTopicFormat(), elements.topic());
    }

    private boolean hasTemplateValueError(String rawTemplate, String format, String value) {
        return rawTemplate.contains(format) && isEmpty(value);
    }

    private boolean isEmpty(String value) {
        return !StringUtils.hasText(value);
    }
}
