package com.mock.interview.interviewquestion.infra.ai.gpt;

/**
 * AI 종류에 따른 스펙을 알려주는 인터페이스
 * oepnAI의 경우 Role은 system, user, assistant가 있고 MaxToken은 4096
 */
public interface AISpecification {
    long CONNECT_TIMEOUT_MS = 2_000;
    long READ_TIMEOUT_MS = 8_000;

    String getSignature();
    String getSystemRole();
    String getUserRole();
    String getInterviewerRole();
    long getMaxToken();
    boolean isTokenLimitExceeded(InterviewAIRequest request);

    // TODO: Message에 토큰 수를 파악하는 메소드도 필요해 보임.
}
