package com.mock.interview.tech.infra.view;

import com.mock.interview.category.infra.support.DefaultCategorySupportChecker;
import com.mock.interview.category.infra.support.ITCategorySupportChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DefaultCategoryRelatedTechFinder extends DefaultCategorySupportChecker implements CategoryRelatedTechFinder {
    private final List<String> defaultTechNames = Collections.emptyList();

    @Override
    public List<String> getRelatedTechName() {
        return defaultTechNames;
    }
}
