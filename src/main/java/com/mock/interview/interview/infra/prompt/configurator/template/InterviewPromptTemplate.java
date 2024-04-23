package com.mock.interview.interview.infra.prompt.configurator.template;

import com.mock.interview.category.infra.support.CategorySupportChecker;

public interface InterviewPromptTemplate extends CategorySupportChecker {
    String getPersonal();
    String getExperience();
    String getTechnical();
}
