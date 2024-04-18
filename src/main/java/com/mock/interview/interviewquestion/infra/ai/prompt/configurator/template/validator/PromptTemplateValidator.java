package com.mock.interview.interviewquestion.infra.ai.prompt.configurator.template.validator;


import com.mock.interview.category.presentation.dto.response.CategoryResponse;
import com.mock.interview.experience.presentation.dto.ExperienceDto;
import com.mock.interview.interviewquestion.infra.ai.prompt.configurator.PromptConfigElements;
import com.mock.interview.tech.presentation.dto.TechnicalSubjectsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @see com.mock.interview.interviewquestion.infra.ai.prompt.fomatter.StringFormatter
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
        return hasTemplateValueError(rawTemplate, formatGetter.getFieldFormat(), elements.field())
                || hasTemplateValueError(rawTemplate, formatGetter.getCategoryFormat(), elements.category())
                || hasTemplateValueError(rawTemplate, formatGetter.getSkillsFormat(), elements.tech())
                || hasTemplateValueError(rawTemplate, formatGetter.getExperienceFormat(), elements.experience());
    }

    private boolean hasTemplateValueError(String rawTemplate, String format, CategoryResponse value) {
        return rawTemplate.contains(format) && isEmpty(value);
    }

    private boolean hasTemplateValueError(String rawTemplate, String format, TechnicalSubjectsResponse value) {
        return rawTemplate.contains(format) && isEmpty(value);
    }

    private boolean hasTemplateValueError(String rawTemplate, String format, ExperienceDto value) {
        return rawTemplate.contains(format) && isEmpty(value);
    }

    private boolean isEmpty(String value) {
        return !StringUtils.hasText(value);
    }

    private boolean isEmpty(CategoryResponse value) {
        return value == null || isEmpty(value.getName());
    }

    private boolean isEmpty(TechnicalSubjectsResponse value) {
        return value == null || isEmpty(value.getName());
    }

    private boolean isEmpty(ExperienceDto value) {
        return value == null || isEmpty(value.getContent());
    }
}
