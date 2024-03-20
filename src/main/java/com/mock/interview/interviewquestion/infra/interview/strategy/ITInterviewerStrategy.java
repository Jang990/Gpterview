package com.mock.interview.interviewquestion.infra.interview.strategy;

import com.mock.interview.interviewquestion.infra.interview.dto.InterviewInfo;
import com.mock.interview.interviewquestion.infra.interview.dto.InterviewProfile;
import com.mock.interview.interviewquestion.infra.interview.dto.PromptCreationInfo;
import com.mock.interview.interviewquestion.infra.interview.gpt.AISpecification;
import com.mock.interview.interviewquestion.infra.interview.setting.AiPrompt;
import com.mock.interview.interviewquestion.infra.interview.setting.PromptCreator;
import com.mock.interview.interviewquestion.infra.interview.strategy.stage.InterviewProgress;
import com.mock.interview.interviewquestion.infra.interview.strategy.stage.InterviewProgressTimeBasedTracker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ITInterviewerStrategy implements InterviewerStrategy {
    private final InterviewProgressTimeBasedTracker progressTracker;
    private final PromptCreator promptCreator;
    private final ITInterviewConcept interviewConcept;

    private final List<String> basicKnowledge = List.of("운영체제", "네트워크", "데이터베이스", "자료구조", "알고리즘");
    private final String[] SUPPORTED_DEPARTMENT = {"IT", "개발"};

    @Override
    public PromptCreationInfo configStrategy(AISpecification aiSpec, InterviewProfile profile, InterviewProgress progress) {
        return switch (progress.stage()) {
            case TECHNICAL -> createTechPromptCreationInfo(
                    interviewConcept.getTechnical(),
                    profile, progress.progress()
            );
            case EXPERIENCE -> createExperiencePromptCreationInfo(
                    interviewConcept.getExperience(),
                    profile, progress.progress()
            );
            case PERSONAL -> createPersonalPromptCreationInfo(
                    interviewConcept.getPersonal(),
                    profile, progress
            );
        };
    }

    @Override
    public AiPrompt changeTopic(AISpecification aiSpec, InterviewInfo interviewInfo) {
        PromptCreationInfo promptCreationInfo = createChangeTopicPromptCreationInfo(interviewInfo);
        return promptCreator.create(aiSpec, promptCreationInfo);
    }

    private PromptCreationInfo createPromptCreationInfo(InterviewInfo interviewInfo) {
        InterviewProfile profile = interviewInfo.profile();
        InterviewProgress currentProgress = progressTracker.getCurrentInterviewProgress(interviewInfo.config());

        return switch (currentProgress.stage()) {
            case TECHNICAL -> createTechPromptCreationInfo(
                    interviewConcept.getTechnical(),
                    profile, currentProgress.progress()
            );
            case EXPERIENCE -> createExperiencePromptCreationInfo(
                    interviewConcept.getExperience(),
                    profile, currentProgress.progress()
            );
            case PERSONAL -> createPersonalPromptCreationInfo(
                    interviewConcept.getPersonal(),
                    profile, currentProgress
            );
        };
    }

    private PromptCreationInfo createChangeTopicPromptCreationInfo(InterviewInfo interviewInfo) {
        InterviewProfile profile = interviewInfo.profile();
        InterviewProgress currentProgress = progressTracker.getCurrentInterviewProgress(interviewInfo.config());

        return switch (currentProgress.stage()) {
            case TECHNICAL -> createTechPromptCreationInfo(
                    interviewConcept.getTechnical() + interviewConcept.getChangingTopicCommand(),
                    profile, currentProgress.progress()
            );
            case EXPERIENCE -> createExperiencePromptCreationInfo(
                    interviewConcept.getExperience() + interviewConcept.getChangingTopicCommand(),
                    profile, currentProgress.progress()
            );
            case PERSONAL -> createPersonalPromptCreationInfo(
                    interviewConcept.getPersonal() + interviewConcept.getChangingTopicCommand(),
                    profile, currentProgress
            );
        };
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

    private PromptCreationInfo createPersonalPromptCreationInfo(String personalPromptTemplate, InterviewProfile profile, InterviewProgress currentProgress) {
        return new PromptCreationInfo(
                personalPromptTemplate,
                profile.department(), profile.field(),
                null, null
        );
    }

    private PromptCreationInfo createExperiencePromptCreationInfo(String experiencePromptTemplate, InterviewProfile profile, double progress) {
        String selectedExperience = selectSkillBasedOnProgress(progress, profile.skills());
        return new PromptCreationInfo(
                experiencePromptTemplate, profile.department(), profile.field(),
                null, selectedExperience
        );
    }

    private PromptCreationInfo createTechPromptCreationInfo(String techPromptTemplate, InterviewProfile profile, double progress) {
        String selectedSkills = selectSkillBasedOnProgress(progress, profile.skills());
        return new PromptCreationInfo(
                techPromptTemplate, profile.department(), profile.field(),
                selectedSkills, null
        );
    }

    private String selectSkillBasedOnProgress(double progress, List<String> candidateSkills) {
        List<String> questionTopicList = new ArrayList<>();
        questionTopicList.addAll(basicKnowledge);
        questionTopicList.addAll(candidateSkills);
        int n = (int) Math.floor(progress * questionTopicList.size());
        return questionTopicList.get(n);
    }
}