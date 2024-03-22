package com.mock.interview.interviewquestion.infra.ai;

import com.mock.interview.interviewquestion.infra.RecommendedQuestion;
import com.mock.interview.interviewquestion.infra.ai.dto.InterviewInfo;
import com.mock.interview.interviewquestion.infra.ai.dto.Message;
import com.mock.interview.interviewquestion.infra.ai.dto.MessageHistory;
import com.mock.interview.interviewquestion.infra.ai.progress.InterviewStage;
import com.mock.interview.interviewquestion.infra.ai.prompt.configurator.PromptConfiguration;
import com.mock.interview.interviewquestion.infra.ai.gpt.AIRequester;
import com.mock.interview.interviewquestion.infra.ai.gpt.InterviewAIRequest;
import com.mock.interview.interviewquestion.infra.ai.prompt.AiPrompt;
import com.mock.interview.interviewquestion.infra.ai.prompt.PromptCreator;
import com.mock.interview.interviewquestion.infra.ai.prompt.configurator.generator.InterviewPromptConfigurator;
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

    public RecommendedQuestion service(InterviewInfo interviewInfo, MessageHistory history) {
        InterviewProgress progress = progressTracker.getCurrentInterviewProgress(interviewInfo.config());
        PromptConfiguration promptConfig = createPromptConfig(progress, interviewInfo);
        AiPrompt prompt = promptCreator.create(requester, promptConfig);

        // TODO: AI에 request 토큰 제한이 있기 때문에 message List에서 필요한 부분만 추출해서 넣어야 함.

        Message response = requester.sendRequest(new InterviewAIRequest(history.getMessages(), prompt));
        return createPublishedQuestion(progress, promptConfig, response);
    }

    private RecommendedQuestion createPublishedQuestion(InterviewProgress progress, PromptConfiguration promptConfig, Message response) {
        List<String> topic = getTopic(progress.stage(), promptConfig);
        return new RecommendedQuestion(requester.getSignature(), response.getContent(), progress, topic);
    }

    private List<String> getTopic(InterviewStage stage, PromptConfiguration promptConfig) {
        return switch (stage) {
            case TECHNICAL -> promptConfig.getSkills();
            case EXPERIENCE -> promptConfig.getExperience();
            case PERSONAL -> List.of();
        };
    }

    private PromptConfiguration createPromptConfig(InterviewProgress progress, InterviewInfo interviewInfo) {
        InterviewPromptConfigurator interviewPromptConfigurator = selectInterviewerStrategy(interviewInfo);
        return interviewPromptConfigurator.configStrategy(requester, interviewInfo.profile(), progress);
    }

    /**
     * 이상한 주제가 와서 사용자가 다른 주제로 변경 요청.
     *
     *  예시
     * 사용자 : 저는 AOP를 모릅니다.
     * 면접관 : AOP를 모르신다니 아쉽습니다. AOP를 활용한 사례를 들어서 설명해보세요.
     */
    public RecommendedQuestion changeTopic(InterviewInfo interviewInfo, MessageHistory history) {
        InterviewProgress progress = progressTracker.getCurrentInterviewProgress(interviewInfo.config());
        PromptConfiguration promptConfig = createPromptConfig(progress, interviewInfo);
        AiPrompt prompt = promptCreator.changeTopic(requester, promptConfig);

        // TODO: AI에 request 토큰 제한이 있기 때문에 message List에서 필요한 부분만 추출해서 넣어야 함.

        Message response = requester.sendRequest(new InterviewAIRequest(history.getMessages(), prompt));
        return createPublishedQuestion(progress, promptConfig, response);
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
