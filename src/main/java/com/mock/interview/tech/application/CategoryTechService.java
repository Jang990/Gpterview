package com.mock.interview.tech.application;

import com.mock.interview.category.domain.exception.JobCategoryNotFoundException;
import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.category.infra.JobCategoryRepository;
import com.mock.interview.category.infra.CategoryModuleFinder;
import com.mock.interview.tech.infra.TechnicalSubjectsRepository;
import com.mock.interview.tech.infra.view.CategoryRelatedTechFinder;
import com.mock.interview.tech.presentation.dto.TechnicalSubjectsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryTechService {
    private final JobCategoryRepository jobCategoryRepository;
    private final TechnicalSubjectsRepository technicalSubjectsRepository;
    private final List<CategoryRelatedTechFinder> categoryRelatedTechFinders;

    public List<TechnicalSubjectsResponse> findRelatedTech(long categoryId) {
        JobCategory category = jobCategoryRepository.findById(categoryId)
                .orElseThrow(JobCategoryNotFoundException::new);
        System.out.println(categoryRelatedTechFinders.size());

        CategoryRelatedTechFinder finder = CategoryModuleFinder
                .findModule(categoryRelatedTechFinders, category.getName());
        return finder.find(technicalSubjectsRepository);
    }

}
