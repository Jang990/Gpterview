package com.mock.interview.infrastructure.interview;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class CommonConstGetter {
    @Value("${interview.format.role.system}")
    private String systemRoleFormat;
    @Value("${interview.format.role.user}")
    private String userRoleFormat;
    @Value("${interview.format.role.interviewer}")
    private String interviewerRoleFormat;
    @Value("${interview.format.profile.department}")
    private String departmentFormat;
    @Value("${interview.format.profile.field}")
    private String fieldFormat;
    @Value("${interview.format.profile.skills}")
    private String skillsFormat;

    @Value("${interview.strategy.common.rule}")
    private String commonRule;
    @Value("${interview.strategy.common.profile}")
    private String commonProfile;
    @Value("${interview.strategy.common.skills}")
    private String commonSkills;
}
