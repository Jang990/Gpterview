package com.mock.interview.interview.infra.prompt.template;

import com.mock.interview.category.infra.support.CategorySupportChecker;
import com.mock.interview.interview.infra.progress.dto.InterviewPhase;

public interface InterviewPromptTemplate extends CategorySupportChecker {
    String getPersonal();
    String getExperience();
    String getTechnical();

    default String getTemplate(InterviewPhase phase) {
        return switch (phase) {
            case TECHNICAL -> getTechnical();
            case EXPERIENCE -> getExperience();
            case PERSONAL -> getPersonal();
        };
    }
}
