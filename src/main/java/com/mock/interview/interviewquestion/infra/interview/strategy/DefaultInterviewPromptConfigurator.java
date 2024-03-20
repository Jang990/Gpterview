package com.mock.interview.interviewquestion.infra.interview.strategy;

import com.mock.interview.interviewquestion.infra.interview.dto.InterviewInfo;
import com.mock.interview.interviewquestion.infra.interview.dto.InterviewProfile;
import com.mock.interview.interviewquestion.infra.interview.dto.PromptConfiguration;
import com.mock.interview.interviewquestion.infra.interview.gpt.AISpecification;
import com.mock.interview.interviewquestion.infra.interview.strategy.stage.InterviewProgress;
import com.mock.interview.interviewquestion.infra.interview.strategy.stage.InterviewStage;
import com.mock.interview.interviewquestion.infra.interview.strategy.template.DefaultInterviewTemplateGetter;
import lombok.RequiredArgsConstructor;

//@Order(1)
//@Component
@RequiredArgsConstructor
public class DefaultInterviewPromptConfigurator implements InterviewPromptConfigurator {
    private final DefaultInterviewTemplateGetter templateGetter;

    // TODO: 수정 많이 필요.
    //      -> IT 먼저 끝내고 바꿀 것.
    @Override
    public PromptConfiguration configStrategy(AISpecification aiSpec, InterviewProfile profile, InterviewProgress progress) {
        String rawStrategy = getRawInterviewStrategy(progress.stage());
        return new PromptConfiguration(
                rawStrategy, profile.department(), profile.field(),
                profile.skills().toString(), profile.experience().toString()
        );
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