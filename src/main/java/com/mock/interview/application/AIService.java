package com.mock.interview.application;

import com.mock.interview.infrastructure.gpt.AISpecification;
import com.mock.interview.infrastructure.gpt.InterviewAIRequest;
import com.mock.interview.infrastructure.gpt.AIRequester;
import com.mock.interview.infrastructure.interview.strategy.ITInterviewerStrategy;
import com.mock.interview.presentaion.web.dto.InterviewInfo;
import com.mock.interview.presentaion.web.dto.InterviewRole;
import com.mock.interview.infrastructure.dto.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AIService {
    private final ITInterviewerStrategy interviewerStrategy;
    private final AIRequester requester;

    public Message service(InterviewInfo interviewInfo) {
        // TODO: 여기서 List<Message>를 건드리면서 화면을 표시할 때 ROLE이 잘못되게 된다. 문제가 많다. DTO 분리를 잘 해줘야해...
        InterviewAIRequest request = interviewerStrategy.configStrategy(requester, interviewInfo); // 면접 전략 세팅.
        System.out.println("=====> 세팅된 전략 : " + request.getInterviewSetting().getConcept());
        convertRole(requester, request.getHistory()); // AIRequester로 보낼 수 있는 role로 수정.
        return requester.sendRequest(request); // AI로 부터 받은 응답.
        // TODO: DB 저장 기능 나중에 추가
    }

    private void convertRole(AISpecification aiSpec, List<Message> history) {
        for (Message message : history) {
            convertRole(aiSpec, message);
        }
    }

    private void convertRole(AISpecification aiSpec, Message message) {
        if(InterviewRole.SYSTEM.toString().equalsIgnoreCase(message.getRole()))
            message.setRole(aiSpec.getSystemRole());
        else if(InterviewRole.INTERVIEWER.toString().equalsIgnoreCase(message.getRole()))
            message.setRole(aiSpec.getInterviewerRole());
        else
            message.setRole(aiSpec.getUserRole());
    }
}
