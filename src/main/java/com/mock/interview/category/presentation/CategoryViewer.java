package com.mock.interview.category.presentation;

import com.mock.interview.category.application.JobCategoryService;
import com.mock.interview.category.application.JobPositionService;
import com.mock.interview.category.presentation.dto.response.CategoryResponse;
import org.springframework.ui.Model;

import java.util.Collections;
import java.util.List;

public final class CategoryViewer {

    private CategoryViewer() {}

    public static final String CATEGORY_LIST_ATTR = "categoryList";
    public static final String POSITION_LIST_ATTR = "positionList";

    private static final int FIRST_IDX = 0;

    public static void showCategoriesView(
            Model model,  Long selectedCategoryId,
            JobCategoryService categoryService, JobPositionService positionService
            // TODO: categoryTechService 사용해서 카테고리에 맞는 기술 추가해 보여줄 것.
    ) {
        List<CategoryResponse> allCategories = categoryService.findAllCategory();
        model.addAttribute(CATEGORY_LIST_ATTR, allCategories);

        verifyExists(selectedCategoryId, allCategories);

        if (selectedCategoryId != null) {
            model.addAttribute(POSITION_LIST_ATTR, getChildPositions(selectedCategoryId, positionService));
        }
        if(allCategories.isEmpty())
            return;
        model.addAttribute(POSITION_LIST_ATTR, getChildPositions(allCategories.get(FIRST_IDX).getId(), positionService));
    }

    private static List<CategoryResponse> getChildPositions(Long selectedCategoryId, JobPositionService positionService) {
        if(selectedCategoryId != null)
            return positionService.findChildPositions(selectedCategoryId);
        else
            return Collections.emptyList();
    }

    private static void verifyExists(Long selectedCategoryId, List<CategoryResponse> allCategories) {
        if(selectedCategoryId == null)
            return;
        boolean categoryIdNoneExists = allCategories.stream().map(CategoryResponse::getId).noneMatch(selectedCategoryId::equals);
        if(allCategories.isEmpty() || categoryIdNoneExists)
            throw new IllegalArgumentException("선택된 카테고리 ID와 일치하는 카테고리가 없음");
    }

}
