package com.mock.interview.interviewquestion.infra.ai.prompt.configurator.template.validator;

import com.mock.interview.category.presentation.dto.response.CategoryResponse;
import com.mock.interview.experience.presentation.dto.ExperienceDto;
import com.mock.interview.interviewquestion.infra.ai.prompt.configurator.PromptConfigElements;
import com.mock.interview.tech.presentation.dto.TechnicalSubjectsResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PromptTemplateValidatorTest {
    @Mock
    TemplateFormatGetter getter;
    @InjectMocks
    PromptTemplateValidator validator;

    private String fieldFormat = "$_field_",
            skillsFormat = "$_skills_",
            departmentFormat = "$_department_",
            experienceFormat = "$_experience_";

    private final String containAllTemplate = fieldFormat + " " + skillsFormat + " "
            + departmentFormat + " " + experienceFormat;

    private final CategoryResponse category = new CategoryResponse(1, "aaa");
    private final CategoryResponse field = new CategoryResponse(2, "bbb");
    private final TechnicalSubjectsResponse tech = new TechnicalSubjectsResponse(3, "ccc");
    private final ExperienceDto experience = new ExperienceDto(4L, "ddd");

    @BeforeEach
    void beforeEach() {
        when(getter.getCategoryFormat()).thenReturn(departmentFormat);
        when(getter.getFieldFormat()).thenReturn(fieldFormat);
        when(getter.getSkillsFormat()).thenReturn(skillsFormat);
        when(getter.getExperienceFormat()).thenReturn(experienceFormat);
    }

    @Test
    @DisplayName("성공 - 정상값")
    void test1() {
        PromptConfigElements configElements = PromptConfigElements.builder()
                .category(category).field(field)
                .tech(tech).experience(experience).build();
        validator.verify(containAllTemplate, configElements);
    }

    @Test
    @DisplayName("실패 - 경험 null")
    void test4() {
        PromptConfigElements configElements = PromptConfigElements.builder()
                .category(category).field(field)
                .tech(tech)
//                .experience(experience)
                .build();
        Assertions.assertThrows(InvalidConfigurationException.class, () -> validator.verify(containAllTemplate, configElements));
    }

    @Test
    @DisplayName("성공 - 템플릿에 없는 값 null 초기화")
    void test6() {
        final String templateWithoutSkill = fieldFormat + " " + departmentFormat + " " + experienceFormat;
        PromptConfigElements configElements = PromptConfigElements.builder()
                .category(category).field(field)
//                .tech(tech)
                .experience(experience)
                .build();
        validator.verify(templateWithoutSkill, configElements);
    }
}