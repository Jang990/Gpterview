package com.mock.interview.interview.infra.interview.setting;

import com.mock.interview.interviewquestion.infra.ai.prompt.PromptCreator;
import com.mock.interview.interview.infra.MockAiSpecCreator;
import com.mock.interview.interviewquestion.infra.ai.prompt.configurator.PromptConfiguration;
import com.mock.interview.interviewquestion.infra.ai.prompt.fomatter.TemplateConstGetter;
import com.mock.interview.interviewquestion.infra.ai.gpt.AISpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AiPromptCreatorTest {
    @InjectMocks PromptCreator creator;
    @Mock TemplateConstGetter getter;

    @BeforeEach
    void before() {
        when(getter.getSystemRole()).thenReturn("system");
        when(getter.getUserRole()).thenReturn("user");
        when(getter.getInterviewerRole()).thenReturn("interviewer");
        when(getter.getField()).thenReturn("field");
        when(getter.getCategory()).thenReturn("department");
        when(getter.getSkills()).thenReturn("skills");
        when(getter.getExperience()).thenReturn("experience");
    }

    @Test
    void success() {
        AISpecification spec = MockAiSpecCreator.createMock();
        PromptConfiguration info = createMockPromptConfig("$_system_ $_user_ $_interviewer_ $_department_ $_field_ $_skills_ $_experience_");

        String result = creator.create(spec, info).getPrompt();

        // system user null IT 백엔드 Java MySQL Spring 나는 ...를 경험했다.
        assertThat(result).contains(spec.getInterviewerRole());
        assertThat(result).contains(spec.getSystemRole());
        assertThat(result).contains(spec.getUserRole());
        assertThat(result).contains(info.getCategory());
        assertThat(result).contains(info.getField());
        assertThat(result).contains(info.getSkills());
        assertThat(result).contains(info.getExperience());
    }

    @Test
    void successWithNull() {
        AISpecification spec = MockAiSpecCreator.createMock();
        PromptConfiguration info = createMockPromptConfig("$_system_ $_user_ $_interviewer_ $_department_ $_WrongKey_ $_field_ $_skills_ ");

        String result = creator.create(spec, info).getPrompt();

        // system user interviewer IT null 백엔드 Java MySQL Spring
        assertThat(result).contains(spec.getInterviewerRole());
        assertThat(result).contains(spec.getSystemRole());
        assertThat(result).contains(spec.getUserRole());
        assertThat(result).contains(info.getCategory());
        assertThat(result).contains(info.getField());
        assertThat(result).contains(info.getSkills());
        assertThat(result).contains("null");
    }

    private PromptConfiguration createMockPromptConfig(String promptTemplate) {
        PromptConfiguration mock = mock(PromptConfiguration.class);
        when(mock.getPromptTemplate()).thenReturn(promptTemplate);
        when(mock.getCategory()).thenReturn("IT");
        when(mock.getField()).thenReturn("백엔드");
        when(mock.getSkills()).thenReturn(List.of("Java"));
        when(mock.getExperience()).thenReturn(List.of("나는 ... 경험했다."));
        return mock;
    }
}