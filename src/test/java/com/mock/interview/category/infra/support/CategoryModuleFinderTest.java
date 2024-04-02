package com.mock.interview.category.infra.support;

import com.mock.interview.category.infra.CategoryModuleFinder;
import com.mock.interview.tech.infra.view.CategoryRelatedTechFinder;
import com.mock.interview.tech.infra.view.DefaultCategoryRelatedTechFinder;
import com.mock.interview.tech.infra.view.ITCategoryRelatedTechFinder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

class CategoryModuleFinderTest {
    private List<CategoryRelatedTechFinder> testFinders = List.of(new ITCategoryRelatedTechFinder(), new DefaultCategoryRelatedTechFinder());
    final String IT_CATEGORY_NAME = "IT";
    final String WEIRD_CATEGORY_NAME = "이상한 카테고리 이름";


    @Test
    @DisplayName("모듈 찾기")
    void test1() {
        String itModuleName = CategoryModuleFinder.findModule(testFinders, IT_CATEGORY_NAME).getClass().getName();
        Assertions.assertThat(itModuleName)
                .isEqualTo(ITCategoryRelatedTechFinder.class.getName());
    }

    @Test
    @DisplayName("디폴트 모듈")
    void test2() {
        String defaultModuleName = CategoryModuleFinder.findModule(testFinders, WEIRD_CATEGORY_NAME).getClass().getName();
        Assertions.assertThat(defaultModuleName)
                .isEqualTo(DefaultCategoryRelatedTechFinder.class.getName());
    }

    @Test
    @DisplayName("디폴트 모듈 누락")
    void test3() {
        List<CategoryRelatedTechFinder> wrongList = List.of(new ITCategoryRelatedTechFinder());
        org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class,
                () -> CategoryModuleFinder.findModule(wrongList, WEIRD_CATEGORY_NAME));
    }

    @Test
    @DisplayName("디폴트 모듈 중복")
    void test4() {
        List<CategoryRelatedTechFinder> duplicateDefaultList = List.of(new DefaultCategoryRelatedTechFinder(), new DefaultCategoryRelatedTechFinder());
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,
                () -> CategoryModuleFinder.findModule(duplicateDefaultList, WEIRD_CATEGORY_NAME));
    }

}