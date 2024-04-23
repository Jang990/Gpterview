package com.mock.interview.interviewquestion.infra.gpt.prompt.config;

import com.mock.interview.interviewquestion.infra.gpt.prompt.template.validator.PromptTemplateValidator;

/**
 * Null이 가능하나 프롬프트에 따라 유효성 검증에서 예외가 발생할 수 있음
 * @see PromptTemplateValidator
 *
 * @param category     IT, 회계 등등
 * @param field          백엔드, 프론트엔드, 안드로이드, IOS 등등
 * @param topic         Spring, Java 등등 or ~경험이 있습니다.
 */
public record PromptElements(String category, String field, String topic) {
}
