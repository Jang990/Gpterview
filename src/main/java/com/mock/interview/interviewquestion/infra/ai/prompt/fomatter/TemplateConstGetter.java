package com.mock.interview.interviewquestion.infra.ai.prompt.fomatter;

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

    @Value("${interview.profile.department}")
    private String department;
    @Value("${interview.profile.field}")
    private String field;
    @Value("${interview.profile.skills}")
    private String skills;
    @Value("${interview.profile.experience}")
    private String experience;

    @Value("${interview.template.common.skip}")
    private String changingTopicCommand;
}
