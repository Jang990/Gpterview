package com.mock.interview.infrastructure.gpt;

import com.mock.interview.presentaion.web.dto.Message;

import java.util.List;

/**
 * AI 종류에 따른 스펙을 알려주는 인터페이스
 * oepnAI의 경우 Role은 system, user, assistant가 있고 MaxToken은 4096
 */
public interface AISpecification {
    String getSystemRole();
    String getUserRole();
    String getInterviewerRole();
    long getMaxToken();
    boolean isTokenLimitExceeded(InterviewAIRequest request);

    // TODO: Message에 토큰 수를 파악하는 메소드도 필요해 보임.
}
