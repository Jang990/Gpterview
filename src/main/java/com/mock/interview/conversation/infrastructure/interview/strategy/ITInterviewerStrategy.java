package com.mock.interview.conversation.infrastructure.interview.strategy;

import com.mock.interview.conversation.infrastructure.interview.dto.*;
import com.mock.interview.conversation.infrastructure.interview.gpt.AISpecification;
import com.mock.interview.conversation.infrastructure.interview.gpt.InterviewAIRequest;
import com.mock.interview.conversation.infrastructure.interview.setting.InterviewSetting;
import com.mock.interview.conversation.infrastructure.interview.setting.InterviewSettingCreator;
import com.mock.interview.conversation.infrastructure.interview.strategy.exception.AlreadyFinishedInterviewException;
import com.mock.interview.conversation.infrastructure.interview.strategy.stage.InterviewProgress;
import com.mock.interview.conversation.infrastructure.interview.strategy.stage.InterviewProgressTimeBasedTracker;
import com.mock.interview.conversation.infrastructure.interview.strategy.stage.InterviewStage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ITInterviewerStrategy implements InterviewerStrategy {
    private final InterviewProgressTimeBasedTracker progressTracker;
    private final InterviewSettingCreator interviewSettingCreator;
    private final ITInterviewConcept interviewConcept;

    private final List<String> basicKnowledge = List.of("운영체제", "네트워크", "데이터베이스", "자료구조", "알고리즘");
    private final String[] SUPPORTED_DEPARTMENT = {"IT", "개발"};

    @Override
    public InterviewSetting configStrategy(AISpecification aiSpec, InterviewInfo interviewInfo) {
        InterviewProfile profile = interviewInfo.profile();
        InterviewProgress currentProgress = progressTracker.getCurrentInterviewProgress(interviewInfo.config());
        String rawStrategy = getRawInterviewStrategy(currentProgress.stage());

        PromptCreationInfo promptCreationInfo = createPromptCreationInfo(rawStrategy, currentProgress, profile);
        return interviewSettingCreator.create(aiSpec, promptCreationInfo);
    }

    private PromptCreationInfo createPromptCreationInfo(String rawStrategy, InterviewProgress currentProgress, InterviewProfile profile) {
        if (currentProgress.stage() == InterviewStage.TECHNICAL) {
            String selectedSkill = selectSkillBasedOnProgress(currentProgress.progress(), profile.skills());
            return new PromptCreationInfo(
                    rawStrategy, profile.department(), profile.field(),
                    selectedSkill, profile.experience().toString()
            );
        }

        // TODO: 다른 Stage도 지원할 것.
        return new PromptCreationInfo(
                rawStrategy, profile.department(), profile.field(),
                profile.skills().get(0), profile.experience().toString()
        );
    }

    private String selectSkillBasedOnProgress(double progress, List<String> candidateSkills) {
        List<String> questionTopicList = new ArrayList<>();
        questionTopicList.addAll(basicKnowledge);
        questionTopicList.addAll(candidateSkills);
        int n = (int) Math.floor(progress * questionTopicList.size());
        return questionTopicList.get(n);
    }

    @Override
    public InterviewSetting changeTopic(AISpecification aiSpec, InterviewInfo interviewInfo) {
        InterviewProfile profile = interviewInfo.profile();
        InterviewProgress currentProgress = progressTracker.getCurrentInterviewProgress(interviewInfo.config());
        String rawStrategy = getRawInterviewStrategy(currentProgress.stage());
        rawStrategy += interviewConcept.getChangingTopicCommand();

        PromptCreationInfo promptCreationInfo = createPromptCreationInfo(rawStrategy, currentProgress, profile);
        return interviewSettingCreator.create(aiSpec, promptCreationInfo);
    }

    @Override
    public boolean isSupportedDepartment(InterviewInfo interviewInfo) {
        if(interviewInfo == null || interviewInfo.profile() == null)
            return false;

        for (String supportedDepartment : SUPPORTED_DEPARTMENT) {
            if(supportedDepartment.equalsIgnoreCase(interviewInfo.profile().department()))
                return true;
        }

        return false;
    }

    private String getRawInterviewStrategy(InterviewStage stage) {
        return switch (stage) {
            case TECHNICAL -> interviewConcept.getTechnical();
            case EXPERIENCE -> interviewConcept.getExperience();
            case PERSONAL -> interviewConcept.getPersonal();
        };
    }
}