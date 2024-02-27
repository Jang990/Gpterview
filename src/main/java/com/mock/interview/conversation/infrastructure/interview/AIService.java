package com.mock.interview.conversation.infrastructure.interview;

import com.mock.interview.conversation.infrastructure.interview.dto.InterviewInfo;
import com.mock.interview.conversation.infrastructure.interview.dto.Message;
import com.mock.interview.conversation.infrastructure.interview.dto.MessageHistory;
import com.mock.interview.conversation.infrastructure.interview.gpt.AIRequester;
import com.mock.interview.conversation.infrastructure.interview.gpt.AISpecification;
import com.mock.interview.conversation.infrastructure.interview.gpt.InterviewAIRequest;
import com.mock.interview.conversation.infrastructure.interview.setting.InterviewSetting;
import com.mock.interview.conversation.infrastructure.interview.strategy.InterviewerStrategy;
import com.mock.interview.conversation.presentation.dto.InterviewRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AIService {
    private final List<InterviewerStrategy> interviewerStrategyList;
    private final AIRequester requester;

    public Message service(InterviewInfo interviewInfo, MessageHistory history) {
        if (history.getMessages().isEmpty()) {
            history = initHistory();
        }

        InterviewerStrategy interviewerStrategy = selectInterviewerStrategy(interviewInfo);
        InterviewSetting setting = interviewerStrategy.configStrategy(requester, interviewInfo); // 면접 전략 세팅.

        // TODO: AI에 request 토큰 제한이 있기 때문에 message List에서 필요한 부분만 추출해서 넣어야 함.

        InterviewAIRequest request = new InterviewAIRequest(history.getMessages(), setting);
        convertRole(requester, request.getHistory()); // AIRequester로 보낼 수 있는 role로 수정.
        return requester.sendRequest(request); // AI로 부터 받은 응답.
    }

    private MessageHistory initHistory() {
        MessageHistory history = new MessageHistory();
        history.getMessages().add(new Message(InterviewRole.AI.toString(), "안녕하세요. 면접을 시작하겠습니다. 준비되셨나요?"));
        history.getMessages().add(new Message(InterviewRole.USER.toString(), "네. 준비됐습니다."));
        return history;
    }

    /**
     * 이상한 주제가 와서 사용자가 다른 주제로 변경 요청.
     *
     *  예시
     * 사용자 : 저는 AOP를 모릅니다.
     * 면접관 : AOP를 모르신다니 아쉽습니다. AOP를 활용한 사례를 들어서 설명해보세요.
     */
    public Message changeTopic(InterviewInfo interviewInfo, MessageHistory history) {
        InterviewerStrategy interviewerStrategy = selectInterviewerStrategy(interviewInfo);
        InterviewSetting setting = interviewerStrategy.changeTopic(requester, interviewInfo);

        // TODO: AI에 request 토큰 제한이 있기 때문에 message List에서 필요한 부분만 추출해서 넣어야 함.

        InterviewAIRequest request = new InterviewAIRequest(history.getMessages(), setting);
        convertRole(requester, request.getHistory());
        return requester.sendRequest(request);
    }

    private InterviewerStrategy selectInterviewerStrategy(InterviewInfo interviewInfo) {
        for (int i = interviewerStrategyList.size() - 1; i >= 0; i--) {
            InterviewerStrategy interviewerStrategy = interviewerStrategyList.get(i);
            if(interviewerStrategy.isSupportedDepartment(interviewInfo))
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
        else if(InterviewRole.AI.toString().equalsIgnoreCase(message.getRole()))
            message.setRole(aiSpec.getInterviewerRole());
        else
            message.setRole(aiSpec.getUserRole());
    }

    public Message serviceTemp() {
        System.out.println("2초대기한다.");
        try {
            Thread.sleep(2_000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return new Message(InterviewRole.AI.toString(), "Hello World!");
    }
}
