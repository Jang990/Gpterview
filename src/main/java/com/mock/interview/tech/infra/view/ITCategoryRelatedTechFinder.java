package com.mock.interview.tech.infra.view;

import com.mock.interview.category.infra.support.ITCategorySupportChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ITCategoryRelatedTechFinder extends ITCategorySupportChecker implements CategoryRelatedTechFinder {
    private final List<String> itRelatedTechName = List.of("운영체제", "네트워크", "데이터베이스", "자료구조", "알고리즘");

    @Override
    public List<String> getRelatedTechName() {
        return itRelatedTechName;
    }
}
