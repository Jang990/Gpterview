package com.mock.interview.interviewquestion.infra.ai.prompt.configurator.generator;

import com.mock.interview.interviewquestion.infra.ai.dto.InterviewInfo;
import com.mock.interview.interviewquestion.infra.ai.dto.InterviewProfile;
import com.mock.interview.interviewquestion.infra.ai.prompt.configurator.PromptConfiguration;
import com.mock.interview.interviewquestion.infra.ai.gpt.AISpecification;
import com.mock.interview.interviewquestion.infra.ai.progress.InterviewProgress;
import com.mock.interview.interviewquestion.infra.ai.prompt.configurator.PromptConfigurationCreator;
import com.mock.interview.interviewquestion.infra.ai.prompt.configurator.template.ITInterviewTemplateGetter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ITInterviewPromptConfigurator implements InterviewPromptConfigurator {
    private final ITInterviewTemplateGetter templateGetter;
    private final PromptConfigurationCreator configurationCreator;

    private final List<String> basicKnowledge = List.of("운영체제", "네트워크", "데이터베이스", "자료구조", "알고리즘");
    private final String[] SUPPORTED_DEPARTMENT = {"IT", "개발"};

    @Override
    public PromptConfiguration configStrategy(AISpecification aiSpec, InterviewProfile profile, InterviewProgress progress) {
        return switch (progress.stage()) {
            case TECHNICAL -> createTechPromptConfig(profile, progress.progress());
            case EXPERIENCE -> createExperiencePromptConfig(profile, progress.progress());
            case PERSONAL -> createPersonalPromptConfig(profile, progress.progress());
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

    private PromptConfiguration createPersonalPromptConfig(InterviewProfile profile, double progress) {
        return configurationCreator.create(
                templateGetter.getPersonal(),
                new InterviewProfile(profile.department(), profile.field(), profile.skills(), profile.experience())
        );
    }

    private PromptConfiguration createExperiencePromptConfig(InterviewProfile profile, double progress) {
        String selectedExperience = selectStringBasedOnProgress(progress, profile.experience());
        return configurationCreator.create(
                templateGetter.getExperience(),
                new InterviewProfile(profile.department(), profile.field(), profile.skills(), List.of(selectedExperience))
        );
    }

    private PromptConfiguration createTechPromptConfig(InterviewProfile profile, double progress) {
        String selectedSkills = selectSkills(progress, profile.skills());
        return configurationCreator.create(
                templateGetter.getTechnical(),
                new InterviewProfile(profile.department(), profile.field(), List.of(selectedSkills), profile.experience())
        );
    }

    private String selectSkills(double progress, List<String> candidateSkills) {
        List<String> questionTopicList = new ArrayList<>();
        questionTopicList.addAll(basicKnowledge);
        questionTopicList.addAll(candidateSkills);
        return selectStringBasedOnProgress(progress, questionTopicList);
    }

    private String selectStringBasedOnProgress(double progress, List<String> list) {
        int n = (int) Math.floor(progress * list.size());
        return list.get(n);
    }
}