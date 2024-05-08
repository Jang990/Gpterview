package com.mock.interview.category.presentation;

import com.mock.interview.category.application.JobCategoryService;
import com.mock.interview.category.application.JobPositionService;
import com.mock.interview.category.presentation.dto.response.CategoryResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryViewerTest {
    Model testModel;
    JobCategoryService categoryService;
    JobPositionService positionService;

    @BeforeEach
    public void beforeEach() {
        testModel = mock(Model.class);
        categoryService = mock(JobCategoryService.class);
        positionService = mock(JobPositionService.class);
    }

    @Test
    @DisplayName("존재하지 않는 카테고리 선택됨")
    void test1() {
        Long testId = 1L;
        when(categoryService.findAllCategory()).thenReturn(Collections.emptyList());
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            CategoryViewer.showCategoriesView(
                    testModel, testId, categoryService, positionService
            );
        });
    }

    @Test
    @DisplayName("카테고리 empty")
    void test2() {
        Long testId = null;
        when(categoryService.findAllCategory()).thenReturn(Collections.emptyList());
        CategoryViewer.showCategoriesView(
                testModel, testId, categoryService, positionService
        );
    }

    @Test
    @DisplayName("카테고리 정상 선택")
    void test3() {
        Long testId = 1L;
        when(categoryService.findAllCategory()).thenReturn(
                List.of(new CategoryResponse(1L,"test"))
        );
        CategoryViewer.showCategoriesView(
                testModel, testId, categoryService, positionService
        );
    }
}