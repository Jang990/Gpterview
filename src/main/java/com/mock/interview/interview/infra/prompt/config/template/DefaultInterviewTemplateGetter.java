package com.mock.interview.interview.infra.prompt.config.template;

import com.mock.interview.category.infra.support.DefaultCategorySupportChecker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DefaultInterviewTemplateGetter extends DefaultCategorySupportChecker implements InterviewPromptTemplate {
    @Value("${interview.template.default.personal}")
    private String personal;
    @Value("${interview.template.default.experience}")
    private String experience;
    @Value("${interview.template.default.technical}")
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
