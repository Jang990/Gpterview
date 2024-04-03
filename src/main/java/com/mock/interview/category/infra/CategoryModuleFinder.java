package com.mock.interview.category.infra;

import com.mock.interview.category.infra.support.CategorySupportChecker;
import com.mock.interview.category.infra.support.DefaultCategorySupportChecker;

import java.util.List;

public abstract class CategoryModuleFinder {
    /** List<CategorySupportChecker>로 직접 말고 하위 인터페이스를 사용할 것 */
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