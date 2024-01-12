package com.mock.interview.interview.infrastructure;

import com.mock.interview.conversation.infrastructure.interview.gpt.AISpecification;
import org.mockito.Mockito;

public class MockAiSpecCreator {
    public static final String SYSTEM = "system";
    public static final String USER = "user";
    public static final String INTERVIEWER = "interviewer";
    public static final long MAX_TOKEN = 4096L;
    public static AISpecification createMock() {
        AISpecification spec = Mockito.mock(AISpecification.class);
        Mockito.when(spec.getSystemRole()).thenReturn(SYSTEM);
        Mockito.when(spec.getUserRole()).thenReturn(USER);
        Mockito.when(spec.getInterviewerRole()).thenReturn(INTERVIEWER);
//        Mockito.when(spec.getMaxToken()).thenReturn(MAX_TOKEN);
        return spec;
    }
}
