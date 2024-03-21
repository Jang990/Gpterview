package com.mock.interview.interviewquestion.infra.ai;

import com.mock.interview.interviewquestion.infra.ai.dto.InterviewInfo;
import com.mock.interview.interviewquestion.infra.ai.dto.Message;
import com.mock.interview.interviewquestion.infra.ai.dto.MessageHistory;
import com.mock.interview.interviewquestion.infra.ai.prompt.PromptConfiguration;
import com.mock.interview.interviewquestion.infra.ai.gpt.AIRequester;
import com.mock.interview.interviewquestion.infra.ai.gpt.InterviewAIRequest;
import com.mock.interview.interviewquestion.infra.ai.prompt.AiPrompt;
import com.mock.interview.interviewquestion.infra.ai.prompt.PromptCreator;
import com.mock.interview.interviewquestion.infra.ai.prompt.configurator.InterviewPromptConfigurator;
import com.mock.interview.interviewquestion.infra.ai.progress.InterviewProgress;
import com.mock.interview.interviewquestion.infra.ai.progress.InterviewProgressTimeBasedTracker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AiQuestionCreator {
    private final List<InterviewPromptConfigurator> interviewPromptConfiguratorList;
    private final AIRequester requester;
    private final InterviewProgressTimeBasedTracker progressTracker;
    private final PromptCreator promptCreator;

    public Message service(InterviewInfo interviewInfo, MessageHistory history) {
        PromptConfiguration promptConfig = createPromptConfig(interviewInfo);
        AiPrompt prompt = promptCreator.create(requester, promptConfig);

        // TODO: AI에 request 토큰 제한이 있기 때문에 message List에서 필요한 부분만 추출해서 넣어야 함.

        InterviewAIRequest request = new InterviewAIRequest(history.getMessages(), prompt);
        return requester.sendRequest(request); // AI로 부터 받은 응답.
    }

    private PromptConfiguration createPromptConfig(InterviewInfo interviewInfo) {
        InterviewPromptConfigurator interviewPromptConfigurator = selectInterviewerStrategy(interviewInfo);
        InterviewProgress progress = progressTracker.getCurrentInterviewProgress(interviewInfo.config());
        return interviewPromptConfigurator.configStrategy(requester, interviewInfo.profile(), progress);
    }

    /**
     * 이상한 주제가 와서 사용자가 다른 주제로 변경 요청.
     *
     *  예시
     * 사용자 : 저는 AOP를 모릅니다.
     * 면접관 : AOP를 모르신다니 아쉽습니다. AOP를 활용한 사례를 들어서 설명해보세요.
     */
    public Message changeTopic(InterviewInfo interviewInfo, MessageHistory history) {
        PromptConfiguration promptConfig = createPromptConfig(interviewInfo);
        AiPrompt prompt = promptCreator.changeTopic(requester, promptConfig);

        // TODO: AI에 request 토큰 제한이 있기 때문에 message List에서 필요한 부분만 추출해서 넣어야 함.

        InterviewAIRequest request = new InterviewAIRequest(history.getMessages(), prompt);
        return requester.sendRequest(request);
    }

    private InterviewPromptConfigurator selectInterviewerStrategy(InterviewInfo interviewInfo) {
        for (int i = interviewPromptConfiguratorList.size() - 1; i >= 0; i--) {
            InterviewPromptConfigurator interviewPromptConfigurator = interviewPromptConfiguratorList.get(i);
            if(interviewPromptConfigurator.isSupportedDepartment(interviewInfo))
                return interviewPromptConfigurator;
        }

        // TODO: 커스텀 예외로 바꿀 것.
        throw new RuntimeException();
    }
}
