package com.mock.interview.interviewquestion.infra.gpt.prompt.fomatter;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class TemplateConstGetter {
    @Value("${interview.role.system}")
    private String systemRole;
    @Value("${interview.role.user}")
    private String userRole;
    @Value("${interview.role.interviewer}")
    private String interviewerRole;

    @Value("${interview.profile.category}")
    private String category;
    @Value("${interview.profile.field}")
    private String field;
    @Value("${interview.profile.topic}")
    private String topic;

    @Value("${interview.template.common.skip}")
    private String changingTopicCommand;
    @Value("${interview.additional.info.prefix}")
    private String additionalInfoPrefix;
}
