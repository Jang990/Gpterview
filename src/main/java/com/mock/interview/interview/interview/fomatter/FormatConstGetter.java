package com.mock.interview.interview.interview.fomatter;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class FormatConstGetter {
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
}
