package com.mock.interview.interviewquestion.infra.ai.prompt.configurator;

import com.mock.interview.interviewquestion.infra.ai.prompt.configurator.template.validator.PromptTemplateValidator;
import com.mock.interview.category.presentation.dto.response.CategoryResponse;
import com.mock.interview.experience.presentation.dto.ExperienceDto;
import com.mock.interview.tech.presentation.dto.TechnicalSubjectsResponse;
import lombok.Builder;

/**
 * Null이 가능하나 프롬프트에 따라 유효성 검증에서 예외가 발생할 수 있음
 * @see PromptTemplateValidator
 *
 * @param category     IT, 회계 등등
 * @param field          백엔드, 프론트엔드, 안드로이드, IOS 등등
 * @param tech         Spring, Java 등등
 * @param experience     ~경험이 있습니다.
 */
@Builder
public record PromptConfigElements(CategoryResponse category, CategoryResponse field, TechnicalSubjectsResponse tech, ExperienceDto experience) {
}
