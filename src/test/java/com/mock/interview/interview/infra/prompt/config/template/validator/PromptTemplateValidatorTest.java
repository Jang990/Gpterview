package com.mock.interview.interview.infra.prompt.config.template.validator;

import com.mock.interview.interview.infra.prompt.config.PromptElements;
import com.mock.interview.interview.infra.prompt.template.validator.InvalidConfigurationException;
import com.mock.interview.interview.infra.prompt.template.validator.PromptTemplateValidator;
import com.mock.interview.interview.infra.prompt.fomatter.TemplateFormatGetter;
import org.junit.jupiter.api.Assertions;
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
            categoryFormat = "$_category_",
            topicFormat = "$_topic_";

    private final String containAllTemplate = fieldFormat + " " + topicFormat + " " + categoryFormat;

    private final String category = "aaa";
    private final String field = "bbb";
    private final String topic = "ccc";


    @Test
    @DisplayName("성공 - 정상값")
    void test1() {
        when(getter.getCategoryFormat()).thenReturn(categoryFormat);
        when(getter.getFieldFormat()).thenReturn(fieldFormat);
        when(getter.getTopicFormat()).thenReturn(topicFormat);


        PromptElements elements = new PromptElements(category, field, topic);
        validator.verify(containAllTemplate, elements);
    }

    @Test
    @DisplayName("실패 - 템플릿에 있는 값인데 null 입력")
    void test4() {
        when(getter.getCategoryFormat()).thenReturn(categoryFormat);

        PromptElements elements = new PromptElements(null, field, topic);
        Assertions.assertThrows(InvalidConfigurationException.class, () -> validator.verify(containAllTemplate, elements));
    }

    @Test
    @DisplayName("성공 - 템플릿에 없는 값이고 null 입력")
    void test6() {
        when(getter.getCategoryFormat()).thenReturn(categoryFormat);
        when(getter.getFieldFormat()).thenReturn(fieldFormat);
        when(getter.getTopicFormat()).thenReturn(topicFormat);

        final String templateWithoutTopic = fieldFormat + " " + categoryFormat;
        PromptElements elements = new PromptElements(category, field, null);
        validator.verify(templateWithoutTopic, elements);
    }
}