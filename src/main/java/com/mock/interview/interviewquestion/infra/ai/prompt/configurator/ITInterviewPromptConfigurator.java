package com.mock.interview.interviewquestion.infra.ai.prompt.configurator;

import com.mock.interview.interviewquestion.infra.ai.dto.InterviewInfo;
import com.mock.interview.interviewquestion.infra.ai.dto.InterviewProfile;
import com.mock.interview.interviewquestion.infra.ai.dto.PromptConfiguration;
import com.mock.interview.interviewquestion.infra.ai.gpt.AISpecification;
import com.mock.interview.interviewquestion.infra.ai.stage.InterviewProgress;
import com.mock.interview.interviewquestion.infra.ai.prompt.configurator.template.ITInterviewTemplateGetter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ITInterviewPromptConfigurator implements InterviewPromptConfigurator {
    private final ITInterviewTemplateGetter templateGetter;

    private final List<String> basicKnowledge = List.of("운영체제", "네트워크", "데이터베이스", "자료구조", "알고리즘");
    private final String[] SUPPORTED_DEPARTMENT = {"IT", "개발"};

    @Override
    public PromptConfiguration configStrategy(AISpecification aiSpec, InterviewProfile profile, InterviewProgress progress) {
        return switch (progress.stage()) {
            case TECHNICAL -> createTechPromptCreationInfo(
                    templateGetter.getTechnical(),
                    profile, progress.progress()
            );
            case EXPERIENCE -> createExperiencePromptCreationInfo(
                    templateGetter.getExperience(),
                    profile, progress.progress()
            );
            case PERSONAL -> createPersonalPromptCreationInfo(
                    templateGetter.getPersonal(),
                    profile, progress
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

    private PromptConfiguration createPersonalPromptCreationInfo(String personalPromptTemplate, InterviewProfile profile, InterviewProgress currentProgress) {
        return new PromptConfiguration(
                personalPromptTemplate,
                profile.department(), profile.field(),
                null, null
        );
    }

    private PromptConfiguration createExperiencePromptCreationInfo(String experiencePromptTemplate, InterviewProfile profile, double progress) {
        String selectedExperience = selectSkillBasedOnProgress(progress, profile.skills());
        return new PromptConfiguration(
                experiencePromptTemplate, profile.department(), profile.field(),
                null, selectedExperience
        );
    }

    private PromptConfiguration createTechPromptCreationInfo(String techPromptTemplate, InterviewProfile profile, double progress) {
        String selectedSkills = selectSkillBasedOnProgress(progress, profile.skills());
        return new PromptConfiguration(
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