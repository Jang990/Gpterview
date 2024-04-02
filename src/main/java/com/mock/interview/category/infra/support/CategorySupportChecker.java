package com.mock.interview.category.infra.support;

import com.mock.interview.interviewquestion.infra.ai.prompt.configurator.generator.ITInterviewPromptConfigurator;
import com.mock.interview.interviewquestion.infra.ai.prompt.configurator.generator.InterviewPromptConfigurator;
import com.mock.interview.tech.infra.view.CategoryRelatedTechFinder;
import com.mock.interview.tech.infra.view.ITCategoryRelatedTechFinder;

import java.util.List;

/**
 * 카테고리 기능 추가 방법
 * 1. 현재 카테고리 Checker 구현
 * 2. 다음 클래스들을 구현하면서 Checker를 상속할 것.
 * @see CategoryRelatedTechFinder
 * @see InterviewPromptConfigurator
 *
 * 예시
 * @see ITCategoryRelatedTechFinder
 * @see ITInterviewPromptConfigurator
 */
public interface CategorySupportChecker {
    List<String> getSupportedCategoryName();

    default boolean supports(String categoryName) {
        final List<String> supportedCategoryName = getSupportedCategoryName();
        for (String supportedCategory : supportedCategoryName) {
            if(categoryName.equals(supportedCategory))
                return true;
        }
        return false;
    }
}
