package com.mock.interview.interview.infrastructure.interview.strategy;

import com.mock.interview.interview.infrastructure.interview.dto.Message;
import com.mock.interview.interview.infrastructure.interview.gpt.AISpecification;
import com.mock.interview.interview.infrastructure.interview.gpt.InterviewAIRequest;
import com.mock.interview.interview.infrastructure.interview.setting.InterviewSettingCreator;
import com.mock.interview.interview.infrastructure.interview.strategy.exception.AlreadyFinishedInterviewException;
import com.mock.interview.interview.infrastructure.interview.strategy.stage.InterviewProgress;
import com.mock.interview.interview.infrastructure.interview.strategy.stage.InterviewProgressTracker;
import com.mock.interview.interview.infrastructure.interview.dto.MessageHistory;
import com.mock.interview.interview.infrastructure.interview.setting.InterviewSetting;
import com.mock.interview.interview.infrastructure.interview.strategy.stage.InterviewStage;
import com.mock.interview.interview.presentation.dto.InterviewSettingDto;
import com.mock.interview.interview.presentation.dto.CandidateProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Order(1)
@Component
@RequiredArgsConstructor
public class DefaultInterviewerStrategy implements InterviewerStrategy {
    private final InterviewProgressTracker progressTracker;
    private final InterviewSettingCreator interviewSettingCreator;
    private final DefaultInterviewConcept interviewConcept;

    @Override
    public InterviewAIRequest configStrategy(AISpecification aiSpec, InterviewSettingDto interviewSettingDto, MessageHistory history) {
        InterviewProgress currentProgress = progressTracker.getCurrentInterviewProgress(interviewSettingDto.getInterviewDetails(), history);
        String rawStrategy = getRawInterviewStrategy(currentProgress.stage());

        List<Message> messageHistory = history.getMessages();

        InterviewSetting setting = interviewSettingCreator.create(aiSpec, interviewSettingDto.getProfile(), rawStrategy);
        return new InterviewAIRequest(messageHistory, setting);
    }

    @Override
    public InterviewAIRequest changeTopic(AISpecification aiSpec, InterviewSettingDto interviewSettingDto, MessageHistory history) {
        InterviewProgress currentProgress = progressTracker.getCurrentInterviewProgress(interviewSettingDto.getInterviewDetails(), history);
        String rawStrategy = getRawInterviewStrategy(currentProgress.stage());
        rawStrategy += interviewConcept.getChangingTopicCommand();


        List<Message> messageHistory = history.getMessages();

        InterviewSetting setting = interviewSettingCreator.create(aiSpec, interviewSettingDto.getProfile(), rawStrategy);
        return new InterviewAIRequest(messageHistory, setting);
    }

    @Override
    public boolean isSupportedDepartment(CandidateProfileDto profile) {
        return true;
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