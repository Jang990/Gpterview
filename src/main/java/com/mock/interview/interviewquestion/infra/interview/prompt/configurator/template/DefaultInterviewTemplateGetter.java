package com.mock.interview.interviewquestion.infra.interview.prompt.configurator.template;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class DefaultInterviewTemplateGetter {
    @Value("${interview.template.default.personal}")
    private String personal;
    @Value("${interview.template.default.experience}")
    private String experience;
    @Value("${interview.template.default.technical}")
    private String technical;

    @Value("${interview.template.common.skip}")
    private String changingTopicCommand;
}
