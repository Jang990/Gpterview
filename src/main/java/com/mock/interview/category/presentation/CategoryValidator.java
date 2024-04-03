package com.mock.interview.category.presentation;

import com.mock.interview.category.presentation.dto.JobCategorySelectedIds;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

public abstract class CategoryValidator {
    public static void validate(BindingResult bindingResult, JobCategorySelectedIds categorySelectedIds) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        if(isCategoryNull(categorySelectedIds) && hasPosition(categorySelectedIds)) {
            bindingResult.addError(new ObjectError("categoryId", "직무와 연관된 분야가 없습니다."));
            throw new BindException(bindingResult);
        }
    }

    private static boolean hasPosition(JobCategorySelectedIds categorySelectedIds) {
        return categorySelectedIds.getPositionId() != null;
    }

    private static boolean isCategoryNull(JobCategorySelectedIds categorySelectedIds) {
        return categorySelectedIds.getCategoryId() == null;
    }
}
