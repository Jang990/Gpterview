package com.mock.interview.category.infra;

import com.mock.interview.category.infra.support.CategorySupportChecker;
import com.mock.interview.category.infra.support.DefaultCategorySupportChecker;

import java.util.List;

public final class CategoryModuleFinder {
    private CategoryModuleFinder() {}

    /** CategorySupportChecker 하위 인터페이스를 사용할 것 - CategorySupportChecker를 직접 사용하는 것은 의미없음 */
    public static <T extends CategorySupportChecker> T findModule(List<T> modules, String categoryName) {
        T defaultModule = null;
        for (T module : modules) {
            if (module instanceof DefaultCategorySupportChecker) {
                if(defaultModule != null)
                    throw new IllegalArgumentException("중복 디폴트 카테고리 문제");
                defaultModule = module;
                continue;
            }

            if(module.supports(categoryName))
                return module;
        }

        if(defaultModule == null)
            throw new IllegalStateException("디폴트 카테고리 문제");

        return defaultModule;
    }
}
