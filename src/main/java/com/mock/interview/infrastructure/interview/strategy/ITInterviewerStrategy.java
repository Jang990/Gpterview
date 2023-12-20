package com.mock.interview.infrastructure.interview.strategy;

import com.mock.interview.domain.Category;
import com.mock.interview.infrastructure.interview.dto.MessageHistory;
import com.mock.interview.infrastructure.interview.gpt.AISpecification;
import com.mock.interview.infrastructure.interview.gpt.InterviewAIRequest;
import com.mock.interview.infrastructure.interview.setting.InterviewSetting;
import com.mock.interview.infrastructure.interview.setting.InterviewSettingCreator;
import com.mock.interview.infrastructure.interview.strategy.exception.AlreadyFinishedInterviewException;
import com.mock.interview.presentation.dto.InterviewSettingDto;
import com.mock.interview.infrastructure.interview.dto.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ITInterviewerStrategy implements InterviewerStrategy {
    private final ITInterviewConcept interviewConcept;
    private final InterviewSettingCreator interviewSettingCreator;

    private final Category[] SUPPORTED_CATEGORY = {Category.IT};

    private static final int CONVERSATION_UNIT = 2;
    private static final int MAX_TECHNICAL_CONVERSATIONS = 7 * CONVERSATION_UNIT;
    private static final int MAX_PROJECT_EXPERIENCE_CONVERSATIONS = 7 * CONVERSATION_UNIT;

    @Override
    public InterviewAIRequest configStrategy(AISpecification aiSpec, InterviewSettingDto interviewSettingDto, MessageHistory history) {
        String rawStrategy = getRawInterviewStrategy(history);

        List<Message> messageHistory = history.getMessages();
        // TODO: AI에 request 토큰 제한이 있기 때문에 message List에서 필요한 부분만 추출해서 넣어야 함.

        InterviewSetting setting = interviewSettingCreator.create(aiSpec, interviewSettingDto.getProfile(), rawStrategy);
        return new InterviewAIRequest(messageHistory, setting);
    }

    @Override
    public InterviewAIRequest changeTopic(AISpecification aiSpec, InterviewSettingDto interviewSettingDto, MessageHistory history) {
        String rawStrategy = getRawInterviewStrategy(history);
        rawStrategy += interviewConcept.getChangingTopicCommand();

        List<Message> messageHistory = history.getMessages();
        // TODO: AI에 request 토큰 제한이 있기 때문에 message List에서 필요한 부분만 추출해서 넣어야 함.

        InterviewSetting setting = interviewSettingCreator.create(aiSpec, interviewSettingDto.getProfile(), rawStrategy);
        return new InterviewAIRequest(messageHistory, setting);
    }

    @Override
    public boolean isSupportedCategory(Category category) {
        for (Category supportedCategory : SUPPORTED_CATEGORY) {
            if(supportedCategory == category)
                return true;
        }

        return false;
    }

    private String getRawInterviewStrategy(MessageHistory history) {
        // TODO: 단계를 분리하는 임시 코드를 적절한 로직으로 수정할 것 - 나중에 Reids 연동하면서 바꿀 것
        String strategy;
        if (isTechStage(history)) {
            System.out.println("경험 면접 전략 실행");
            strategy = interviewConcept.getTechnical();
        } else if (isExperienceStage(history)) {
            strategy = interviewConcept.getExperience();
        } else {
            System.out.println("인성 면접 전략 실행");
            strategy = interviewConcept.getPersonal();
        }

        return strategy;
    }

    private boolean isExperienceStage(MessageHistory history) {
        return !isTechStage(history) && history.getMessages().size() - MAX_TECHNICAL_CONVERSATIONS <= MAX_PROJECT_EXPERIENCE_CONVERSATIONS;
    }

    private static boolean isTechStage(MessageHistory history) {
        return history.getMessages().size() <= MAX_TECHNICAL_CONVERSATIONS;
    }
}