package com.mock.interview.category.presentation;

import com.mock.interview.category.application.JobCategoryService;
import com.mock.interview.category.application.JobPositionService;
import com.mock.interview.category.presentation.dto.response.CategoryResponse;
import org.springframework.ui.Model;

import java.util.List;

public abstract class CategoryViewer {
    private static final int FIRST_IDX = 0;

    public static void setCategoriesView(
            Model model, JobCategoryService categoryService, JobPositionService positionService
    ) {
        List<CategoryResponse> allCategories = categoryService.findAllCategory();
        model.addAttribute("categoryList", allCategories);
        if(allCategories.isEmpty())
            return;
        model.addAttribute("positionList", positionService.findChildPositions(allCategories.get(FIRST_IDX).getId()));
    }

    public static void setCategoriesView(
            Model model, JobCategoryService categoryService,
            JobPositionService positionService, Long categoryId
    ) {
        List<CategoryResponse> allCategories = categoryService.findAllCategory();
        model.addAttribute("categoryList", allCategories);
        if(allCategories.isEmpty() || categoryId == null)
            return;
        model.addAttribute("positionList", positionService.findChildPositions(categoryId));
    }
}
