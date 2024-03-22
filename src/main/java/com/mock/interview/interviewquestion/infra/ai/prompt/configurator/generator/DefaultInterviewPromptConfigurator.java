package com.mock.interview.interviewquestion.infra.ai.prompt.configurator.generator;

import com.mock.interview.interviewquestion.infra.ai.dto.InterviewInfo;
import com.mock.interview.interviewquestion.infra.ai.dto.InterviewProfile;
import com.mock.interview.interviewquestion.infra.ai.prompt.configurator.PromptConfiguration;
import com.mock.interview.interviewquestion.infra.ai.gpt.AISpecification;
import com.mock.interview.interviewquestion.infra.ai.progress.InterviewProgress;
import com.mock.interview.interviewquestion.infra.ai.progress.InterviewStage;
import com.mock.interview.interviewquestion.infra.ai.prompt.configurator.PromptConfigurationCreator;
import com.mock.interview.interviewquestion.infra.ai.prompt.configurator.template.DefaultInterviewTemplateGetter;
import lombok.RequiredArgsConstructor;

//@Order(1)
//@Component
@RequiredArgsConstructor
public class DefaultInterviewPromptConfigurator implements InterviewPromptConfigurator {
    private final DefaultInterviewTemplateGetter templateGetter;
    private final PromptConfigurationCreator configurationCreator;

    // TODO: 수정 많이 필요.
    //      -> IT 먼저 끝내고 바꿀 것.
    @Override
    public PromptConfiguration configStrategy(AISpecification aiSpec, InterviewProfile profile, InterviewProgress progress) {
        String rawStrategy = getRawInterviewStrategy(progress.stage());
        return configurationCreator.create(rawStrategy, profile);
    }

    @Override
    public boolean isSupportedDepartment(InterviewInfo interviewInfo) {
        return true;
    }

    private String getRawInterviewStrategy(InterviewStage stage) {
        return switch (stage) {
            case TECHNICAL -> templateGetter.getTechnical();
            case EXPERIENCE -> templateGetter.getExperience();
            case PERSONAL -> templateGetter.getPersonal();
        };
    }
}