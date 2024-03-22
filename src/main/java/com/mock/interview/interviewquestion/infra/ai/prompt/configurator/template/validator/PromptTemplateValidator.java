package com.mock.interview.interviewquestion.infra.ai.prompt.configurator.template.validator;


import com.mock.interview.interviewquestion.infra.ai.dto.InterviewProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @see com.mock.interview.interviewquestion.infra.ai.prompt.fomatter.StringFormatter
 * StringFormatter를 통해 Prompt를 만들 때 템플릿 필수 정보를 설정하는 인터페이스
 */
@Component
@RequiredArgsConstructor
public class PromptTemplateValidator {
    private final TemplateFormatGetter formatGetter;

    public void verify(String rawTemplate, InterviewProfile profile) {
        if(hasError(rawTemplate, profile))
            throw new InvalidConfigurationException();
    }

    private boolean hasError(String rawTemplate, InterviewProfile profile) {
        return hasTemplateValueError(rawTemplate, formatGetter.getFieldFormat(), profile.field())
                || hasTemplateValueError(rawTemplate, formatGetter.getDepartmentFormat(), profile.department())
                || hasTemplateValueError(rawTemplate, formatGetter.getSkillsFormat(), profile.skills())
                || hasTemplateValueError(rawTemplate, formatGetter.getExperienceFormat(), profile.experience());
    }

    private boolean hasTemplateValueError(String rawTemplate, String format, String value) {
        return rawTemplate.contains(format) && isEmpty(value);
    }

    private boolean hasTemplateValueError(String rawTemplate, String format, List<String> value) {
        return rawTemplate.contains(format) && isEmpty(value);
    }

    private boolean isEmpty(String value) {
        return !StringUtils.hasText(value);
    }

    private boolean isEmpty(List<String> value) {
        return value == null || value.isEmpty() || value.stream().noneMatch(StringUtils::hasText);
    }
}
