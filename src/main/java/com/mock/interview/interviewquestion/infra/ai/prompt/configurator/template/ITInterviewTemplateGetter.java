package com.mock.interview.interviewquestion.infra.ai.prompt.configurator.template;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ITInterviewTemplateGetter {
    @Value("${interview.template.IT.personal}")
    private String personal;
    @Value("${interview.template.IT.experience}")
    private String experience;
    @Value("${interview.template.IT.technical}")
    private String technical;

    @Value("${interview.template.common.skip}")
    private String changingTopicCommand;
}
