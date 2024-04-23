package com.mock.interview.interviewquestion.infra.gpt.prompt.template.validator;


import com.mock.interview.interviewquestion.infra.gpt.prompt.config.PromptElements;
import com.mock.interview.interviewquestion.infra.gpt.prompt.fomatter.TemplateFormatGetter;
import com.mock.interview.interviewquestion.infra.gpt.prompt.fomatter.StringFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @see StringFormatter
 * StringFormatter를 통해 Prompt를 만들 때 템플릿 필수 정보를 설정하는 인터페이스
 */
@Component
@RequiredArgsConstructor
public class PromptTemplateValidator {
    private final TemplateFormatGetter formatGetter;

    public void verify(String rawTemplate, PromptElements elements) {
        if(hasError(rawTemplate, elements))
            throw new InvalidConfigurationException();
    }

    private boolean hasError(String rawTemplate, PromptElements elements) {
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
