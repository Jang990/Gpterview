package com.mock.interview.interview.infra.interview.setting;

import com.mock.interview.interviewquestion.infra.gpt.prompt.AiPrompt;
import com.mock.interview.interviewquestion.infra.gpt.prompt.PromptCreator;
import com.mock.interview.interview.infra.MockAiSpecCreator;
import com.mock.interview.interviewquestion.infra.gpt.prompt.config.PromptConfig;
import com.mock.interview.interviewquestion.infra.gpt.prompt.fomatter.TemplateConstGetter;
import com.mock.interview.interviewquestion.infra.gpt.AISpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        when(getter.getTopic()).thenReturn("topic");
    }

    @Test
    void success() {
        AISpecification spec = MockAiSpecCreator.createMock();
        PromptConfig info = createMockPromptConfig("$_system_ $_user_ $_interviewer_ $_department_ $_field_ $_topic_");

        String result = creator.create(spec, info).getPrompt();

        // system user interviewer IT 백엔드 Java
        System.out.println(result);
        assertThat(result).contains(spec.getInterviewerRole());
        assertThat(result).contains(spec.getSystemRole());
        assertThat(result).contains(spec.getUserRole());
        assertThat(result).contains(info.getCategory());
        assertThat(result).contains(info.getField());
        assertThat(result).contains(info.getTopic());
    }

    @Test
    void successWithNull() {
        AISpecification spec = MockAiSpecCreator.createMock();
        PromptConfig info = createMockPromptConfig("$_system_ $_user_ $_interviewer_ $_department_ $_WrongKey_ $_field_ $_topic_ ");

        String result = creator.create(spec, info).getPrompt();

        // system user interviewer IT 백엔드 Java
        assertThat(result).contains(spec.getInterviewerRole());
        assertThat(result).contains(spec.getSystemRole());
        assertThat(result).contains(spec.getUserRole());
        assertThat(result).contains(info.getCategory());
        assertThat(result).contains(info.getField());
        assertThat(result).contains(info.getTopic());
        assertThat(result).contains("null");
    }

    @Test
    void successWithLargeTopic() {
        when(getter.getAdditionalInfoPrefix()).thenReturn("지원자 정보:");

        AISpecification spec = MockAiSpecCreator.createMock();
        PromptConfig info = createMockPromptConfig("$_system_ $_user_ $_interviewer_ $_department_ $_field_ $_topic_");
        String largeTopic = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA_SOMTHING";
        when(info.getTopic()).thenReturn(largeTopic);

        AiPrompt prompt = creator.create(spec, info);
        assertThat(prompt.getAdditionalUserInfo()).contains(largeTopic);
    }

    private PromptConfig createMockPromptConfig(String promptTemplate) {
        PromptConfig mock = mock(PromptConfig.class);
        when(mock.getPromptTemplate()).thenReturn(promptTemplate);
        when(mock.getCategory()).thenReturn("IT");
        when(mock.getField()).thenReturn("백엔드");
        when(mock.getTopic()).thenReturn("Java"); // or "나는 ... 경험했다."
        return mock;
    }
}