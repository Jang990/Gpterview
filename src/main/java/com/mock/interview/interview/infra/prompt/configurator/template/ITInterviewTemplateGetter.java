package com.mock.interview.interview.infra.prompt.configurator.template;

import com.mock.interview.category.infra.support.ITCategorySupportChecker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ITInterviewTemplateGetter extends ITCategorySupportChecker implements InterviewPromptTemplate {
    @Value("${interview.template.IT.personal}")
    private String personal;
    @Value("${interview.template.IT.experience}")
    private String experience;
    @Value("${interview.template.IT.technical}")
    private String technical;

    @Override
    public String getPersonal() {
        return personal;
    }

    @Override
    public String getExperience() {
        return experience;
    }

    @Override
    public String getTechnical() {
        return technical;
    }
}
