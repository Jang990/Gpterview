package com.mock.interview.application;

import com.mock.interview.infrastructure.gpt.InterviewAIRequest;
import com.mock.interview.infrastructure.gpt.AIRequester;
import com.mock.interview.infrastructure.interview.strategy.ITInterviewerStrategy;
import com.mock.interview.presentaion.web.dto.InterviewInfo;
import com.mock.interview.presentaion.web.dto.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AIService {
    private final ITInterviewerStrategy interviewerStrategy;
    private final AIRequester requester;

    public Message service(InterviewInfo interviewInfo) {
        InterviewAIRequest request = interviewerStrategy.configStrategy(requester, interviewInfo);
        Message responseMessage = requester.sendRequest(request);
        // TODO: DB 저장 기능 나중에 추가

        return responseMessage;
    }
}
