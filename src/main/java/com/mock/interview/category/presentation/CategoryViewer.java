package com.mock.interview.category.presentation;

import com.mock.interview.category.application.JobCategoryService;
import com.mock.interview.category.application.JobPositionService;
import com.mock.interview.category.presentation.dto.response.CategoryResponse;
import com.mock.interview.tech.application.CategoryTechService;
import org.springframework.ui.Model;

import java.util.List;

public final class CategoryViewer {
    private CategoryViewer() {}

    private static final int FIRST_IDX = 0;

    public static void initInterviewFormPage(
            Model model, Long selectedCategoryId,
            JobCategoryService categoryService, JobPositionService positionService
            // TODO: categoryTechService 사용해서 카테고리에 맞는 기술 추가해 보여줄 것.
    ) {
        List<CategoryResponse> allCategories = categoryService.findAllCategory();
        model.addAttribute("categoryList", allCategories);
        List<CategoryResponse> childPositions = getChildPositions(positionService, allCategories, selectedCategoryId);
        model.addAttribute("positionList", childPositions);
    }

    private static List<CategoryResponse> getChildPositions(JobPositionService positionService, List<CategoryResponse> allCategories, Long selectedCategoryId) {
        if(selectedCategoryId == null)
            return positionService.findChildPositions(allCategories.get(FIRST_IDX).getId());
        else
            return positionService.findChildPositions(selectedCategoryId);
    }

    public static void setCategoriesView(
            Model model, JobCategoryService categoryService, JobPositionService positionService
    ) {
        List<CategoryResponse> allCategories = categoryService.findAllCategory();
        model.addAttribute("categoryList", allCategories);
        if(allCategories.isEmpty())
            return;
        model.addAttribute("positionList", positionService.findChildPositions(allCategories.get(FIRST_IDX).getId()));
    }

}
