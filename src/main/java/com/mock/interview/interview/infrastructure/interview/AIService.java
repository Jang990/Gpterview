package com.mock.interview.interview.infrastructure.interview;

import com.mock.interview.interview.infrastructure.interview.dto.Message;
import com.mock.interview.interview.infrastructure.interview.dto.MessageHistory;
import com.mock.interview.interview.infrastructure.interview.gpt.AIRequester;
import com.mock.interview.interview.infrastructure.interview.gpt.AISpecification;
import com.mock.interview.interview.infrastructure.interview.gpt.InterviewAIRequest;
import com.mock.interview.interview.infrastructure.interview.strategy.InterviewerStrategy;
import com.mock.interview.interview.presentation.dto.CandidateProfileForm;
import com.mock.interview.interview.presentation.dto.InterviewRole;
import com.mock.interview.interview.presentation.dto.InterviewSettingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AIService {
    private final List<InterviewerStrategy> interviewerStrategyList;
    private final AIRequester requester;

    public Message service(InterviewSettingDto interviewSettingDto, MessageHistory history) {
        InterviewerStrategy interviewerStrategy = selectInterviewerStrategy(interviewSettingDto.getProfile());
        InterviewAIRequest request = interviewerStrategy.configStrategy(requester, interviewSettingDto, history); // 면접 전략 세팅.
        convertRole(requester, request.getHistory()); // AIRequester로 보낼 수 있는 role로 수정.
        return requester.sendRequest(request); // AI로 부터 받은 응답.
        // TODO: DB 저장 기능 나중에 추가
    }

    /**
     * 이상한 주제가 와서 사용자가 다른 주제로 변경 요청.
     *
     *  예시
     * 사용자 : 저는 AOP를 모릅니다.
     * 면접관 : AOP를 모르신다니 아쉽습니다. AOP를 활용한 사례를 들어서 설명해보세요.
     */
    public Message changeTopic(InterviewSettingDto interviewSettingDto, MessageHistory history) {
        InterviewerStrategy interviewerStrategy = selectInterviewerStrategy(interviewSettingDto.getProfile());
        InterviewAIRequest request = interviewerStrategy.changeTopic(requester, interviewSettingDto, history);
        convertRole(requester, request.getHistory());
        return requester.sendRequest(request);
    }

    private InterviewerStrategy selectInterviewerStrategy(CandidateProfileForm profile) {
        for (int i = interviewerStrategyList.size() - 1; i >= 0; i--) {
            InterviewerStrategy interviewerStrategy = interviewerStrategyList.get(i);
            if(interviewerStrategy.isSupportedDepartment(profile))
                return interviewerStrategy;
        }

        // TODO: 커스텀 예외로 바꿀 것.
        throw new RuntimeException();
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
