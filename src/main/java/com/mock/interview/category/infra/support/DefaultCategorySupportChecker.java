package com.mock.interview.category.infra.support;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class DefaultCategorySupportChecker implements CategorySupportChecker{
    private final List<String> SUPPORTED_CATEGORY = Collections.emptyList();
    @Override
    public List<String> getSupportedCategoryName() {
        return SUPPORTED_CATEGORY;
    }

    @Override
    public boolean supports(String categoryName) {
        return true;
    }
}
