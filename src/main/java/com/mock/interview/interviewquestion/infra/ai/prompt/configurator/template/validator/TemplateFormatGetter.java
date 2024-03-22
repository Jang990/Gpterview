package com.mock.interview.interviewquestion.infra.ai.prompt.configurator.template.validator;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class TemplateFormatGetter {
    @Value("${interview.format.profile.field}")
    private String fieldFormat;

    @Value("${interview.format.profile.skills}")
    private String skillsFormat;

    @Value("${interview.format.profile.department}")
    private String departmentFormat;

    @Value("${interview.format.profile.experience}")
    private String experienceFormat;
}
