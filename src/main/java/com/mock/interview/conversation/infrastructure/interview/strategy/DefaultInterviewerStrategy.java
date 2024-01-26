package com.mock.interview.conversation.infrastructure.interview.strategy;

import com.mock.interview.conversation.infrastructure.interview.dto.InterviewInfo;
import com.mock.interview.conversation.infrastructure.interview.dto.InterviewProfile;
import com.mock.interview.conversation.infrastructure.interview.dto.Message;
import com.mock.interview.conversation.infrastructure.interview.dto.PromptCreationInfo;
import com.mock.interview.conversation.infrastructure.interview.gpt.AISpecification;
import com.mock.interview.conversation.infrastructure.interview.gpt.InterviewAIRequest;
import com.mock.interview.conversation.infrastructure.interview.setting.InterviewSettingCreator;
import com.mock.interview.conversation.infrastructure.interview.strategy.exception.AlreadyFinishedInterviewException;
import com.mock.interview.conversation.infrastructure.interview.strategy.stage.InterviewProgress;
import com.mock.interview.conversation.infrastructure.interview.strategy.stage.InterviewProgressTimeBasedTracker;
import com.mock.interview.conversation.infrastructure.interview.dto.MessageHistory;
import com.mock.interview.conversation.infrastructure.interview.setting.InterviewSetting;
import com.mock.interview.conversation.infrastructure.interview.strategy.stage.InterviewStage;
import lombok.RequiredArgsConstructor;

import java.util.List;

//@Order(1)
//@Component
@RequiredArgsConstructor
public class DefaultInterviewerStrategy implements InterviewerStrategy {
    private final InterviewProgressTimeBasedTracker progressTracker;
    private final InterviewSettingCreator interviewSettingCreator;
    private final DefaultInterviewConcept interviewConcept;

    // TODO: 수정 많이 필요.
    //      -> IT 먼저 끝내고 바꿀 것.

    @Override
    public InterviewSetting configStrategy(AISpecification aiSpec, InterviewInfo interviewInfo) {
        InterviewProgress currentProgress = progressTracker.getCurrentInterviewProgress(interviewInfo.config());
        String rawStrategy = getRawInterviewStrategy(currentProgress.stage());
        InterviewProfile profile = interviewInfo.profile();

        return createSetting(aiSpec, rawStrategy, profile);
    }
    private InterviewSetting createSetting(AISpecification aiSpec, String rawStrategy, InterviewProfile profile) {
        PromptCreationInfo creationInfo = new PromptCreationInfo(
                rawStrategy, profile.department(), profile.field(),
                profile.skills().toString(), profile.experience().toString()
        );
        return interviewSettingCreator.create(aiSpec, creationInfo);
    }

    @Override
    public InterviewSetting changeTopic(AISpecification aiSpec, InterviewInfo interviewInfo) {
        InterviewProgress currentProgress = progressTracker.getCurrentInterviewProgress(interviewInfo.config());
        String rawStrategy = getRawInterviewStrategy(currentProgress.stage());
        rawStrategy += interviewConcept.getChangingTopicCommand();
        InterviewProfile profile = interviewInfo.profile();

        return createSetting(aiSpec, rawStrategy, profile);
    }

    @Override
    public boolean isSupportedDepartment(InterviewInfo interviewInfo) {
        return true;
    }

    private String getRawInterviewStrategy(InterviewStage stage) {
        return switch (stage) {
            case TECHNICAL -> interviewConcept.getTechnical();
            case EXPERIENCE -> interviewConcept.getExperience();
            case PERSONAL -> interviewConcept.getPersonal();
        };
    }
}