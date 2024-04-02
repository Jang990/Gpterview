package com.mock.interview.category.infra.support;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ITCategorySupportChecker implements CategorySupportChecker{
    private final List<String> SUPPORTED_CATEGORY = List.of("IT", "개발");
    @Override
    public List<String> getSupportedCategoryName() {
        return SUPPORTED_CATEGORY;
    }
}
