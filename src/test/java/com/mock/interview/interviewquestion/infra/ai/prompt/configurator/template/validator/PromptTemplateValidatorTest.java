package com.mock.interview.interviewquestion.infra.ai.prompt.configurator.template.validator;

import com.mock.interview.interviewquestion.infra.ai.dto.InterviewProfile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

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
        InterviewProfile containAllProfile = new InterviewProfile("aaa", "bbb", List.of("ccc"), List.of("ddd"));
        validator.verify(containAllTemplate, containAllProfile);
    }

    @Test
    @DisplayName("성공 - 하나라도 있는 리스트")
    void test2() {
        InterviewProfile containAllProfile = new InterviewProfile("aaa", "bbb", List.of("ccc"), List.of("", "ddd",""));
        validator.verify(containAllTemplate, containAllProfile);
    }

    @Test
    @DisplayName("실패 - 비어있는 리스트 테스트")
    void test3() {
        InterviewProfile containAllProfile = new InterviewProfile("aaa", "bbb", List.of("ccc"), List.of("", ""));
        Assertions.assertThrows(InvalidConfigurationException.class, () -> validator.verify(containAllTemplate, containAllProfile));
    }

    @Test
    @DisplayName("실패 - null 리스트")
    void test4() {
        InterviewProfile containAllProfile = new InterviewProfile("aaa", "bbb", List.of("ccc"), null);
        Assertions.assertThrows(InvalidConfigurationException.class, () -> validator.verify(containAllTemplate, containAllProfile));
    }

    @Test
    @DisplayName("실패 - empty 리스트")
    void test5() {
        InterviewProfile containAllProfile = new InterviewProfile("aaa", "bbb", List.of("ccc"), List.of());
        Assertions.assertThrows(InvalidConfigurationException.class, () -> validator.verify(containAllTemplate, containAllProfile));
    }

    @Test
    @DisplayName("성공 - 템플릿에 없는 값 null 초기화")
    void test6() {
        final String templateWithoutSkill = fieldFormat + " " + departmentFormat + " " + experienceFormat;
        InterviewProfile profileWithoutSkill = new InterviewProfile("aaa", "bbb", null, List.of("ddd"));
        validator.verify(templateWithoutSkill, profileWithoutSkill);
    }
}