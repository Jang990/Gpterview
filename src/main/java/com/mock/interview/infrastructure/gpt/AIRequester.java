package com.mock.interview.infrastructure.gpt;

import com.mock.interview.presentaion.web.dto.Message;

/**
 * AI별 인터뷰 Requester
 */
public interface AIRequester extends AISpecification {
    Message sendRequest(InterviewAIRequest request);
}
