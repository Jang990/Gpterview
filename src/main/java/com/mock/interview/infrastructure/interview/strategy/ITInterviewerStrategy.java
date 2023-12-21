package com.mock.interview.infrastructure.interview.strategy;

import com.mock.interview.domain.Category;
import com.mock.interview.infrastructure.interview.dto.MessageHistory;
import com.mock.interview.infrastructure.interview.gpt.AISpecification;
import com.mock.interview.infrastructure.interview.gpt.InterviewAIRequest;
import com.mock.interview.infrastructure.interview.setting.InterviewSetting;
import com.mock.interview.infrastructure.interview.setting.InterviewSettingCreator;
import com.mock.interview.infrastructure.interview.strategy.exception.AlreadyFinishedInterviewException;
import com.mock.interview.infrastructure.interview.strategy.stage.InterviewProgressTracker;
import com.mock.interview.infrastructure.interview.strategy.stage.InterviewStage;
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
    private final InterviewProgressTracker progressTracker;

    private final String[] SUPPORTED_DEPARTMENT = {Category.IT.getName(), "개발"};

    @Override
    public InterviewAIRequest configStrategy(AISpecification aiSpec, InterviewSettingDto interviewSettingDto, MessageHistory history) {
        InterviewStage currentStage = progressTracker.getCurrentInterviewStage(interviewSettingDto.getInterviewDetails(), history);
        String rawStrategy = getRawInterviewStrategy(currentStage);

        List<Message> messageHistory = history.getMessages();
        // TODO: AI에 request 토큰 제한이 있기 때문에 message List에서 필요한 부분만 추출해서 넣어야 함.

        InterviewSetting setting = interviewSettingCreator.create(aiSpec, interviewSettingDto.getProfile(), rawStrategy);
        return new InterviewAIRequest(messageHistory, setting);
    }

    @Override
    public InterviewAIRequest changeTopic(AISpecification aiSpec, InterviewSettingDto interviewSettingDto, MessageHistory history) {
        InterviewStage currentInterviewStage = progressTracker.getCurrentInterviewStage(interviewSettingDto.getInterviewDetails(), history);
        String rawStrategy = getRawInterviewStrategy(currentInterviewStage);
        rawStrategy += interviewConcept.getChangingTopicCommand();

        List<Message> messageHistory = history.getMessages();
        // TODO: AI에 request 토큰 제한이 있기 때문에 message List에서 필요한 부분만 추출해서 넣어야 함.

        InterviewSetting setting = interviewSettingCreator.create(aiSpec, interviewSettingDto.getProfile(), rawStrategy);
        return new InterviewAIRequest(messageHistory, setting);
    }

    @Override
    public boolean isSupportedDepartment(String department) {
        for (String supportedDepartment : SUPPORTED_DEPARTMENT) {
            if(supportedDepartment.equalsIgnoreCase(department))
                return true;
        }

        return false;
    }

    private String getRawInterviewStrategy(InterviewStage stage) {
        return switch (stage) {
            case TECHNICAL -> interviewConcept.getTechnical();
            case EXPERIENCE -> interviewConcept.getExperience();
            case PERSONAL -> interviewConcept.getPersonal();
            case FINISHED -> throw new AlreadyFinishedInterviewException();
        };
    }
}