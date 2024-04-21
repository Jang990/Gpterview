package com.mock.interview.tech.infra.view;

import com.mock.interview.category.infra.support.CategorySupportChecker;
import com.mock.interview.tech.application.helper.TechConvertHelper;
import com.mock.interview.tech.application.helper.TechSavingHelper;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import com.mock.interview.tech.infra.TechnicalSubjectsRepository;
import com.mock.interview.tech.presentation.dto.TechnicalSubjectsResponse;

import java.util.List;

/**
 * 모의 면접 진행 시 카테고리와 관련된 기술을 지원자 기술에 세팅해서
 * 해당 주제로 면접을 진행할 수 있도록 도움
 */
public interface CategoryRelatedTechFinder extends CategorySupportChecker {
    List<String> getRelatedTechName();

    default List<TechnicalSubjectsResponse> find(TechnicalSubjectsRepository repository) {
        final List<String> relatedTechName = getRelatedTechName();
        List<TechnicalSubjects> technicalSubjects = TechSavingHelper.saveTechIfNotExist(repository, relatedTechName);
        return TechConvertHelper.convert(technicalSubjects);
    }
}
