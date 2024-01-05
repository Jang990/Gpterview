package com.mock.interview.interview.infrastructure.interview.strategy;

import com.mock.interview.interview.domain.Category;
import com.mock.interview.interview.infrastructure.interview.gpt.AISpecification;
import com.mock.interview.interview.infrastructure.interview.gpt.InterviewAIRequest;
import com.mock.interview.interview.infrastructure.interview.dto.MessageHistory;
import com.mock.interview.interview.infrastructure.interview.setting.InterviewSetting;
import com.mock.interview.interview.infrastructure.interview.setting.InterviewSettingCreator;
import com.mock.interview.interview.infrastructure.interview.strategy.exception.AlreadyFinishedInterviewException;
import com.mock.interview.interview.infrastructure.interview.strategy.stage.InterviewProgress;
import com.mock.interview.interview.infrastructure.interview.strategy.stage.InterviewProgressTracker;
import com.mock.interview.interview.infrastructure.interview.strategy.stage.InterviewStage;
import com.mock.interview.interview.presentation.dto.CandidateProfileForm;
import com.mock.interview.interview.presentation.dto.InterviewSettingDto;
import com.mock.interview.interview.infrastructure.interview.dto.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ITInterviewerStrategy implements InterviewerStrategy {
    private final InterviewProgressTracker progressTracker;
    private final InterviewSettingCreator interviewSettingCreator;
    private final ITInterviewConcept interviewConcept;

    private final List<String> basicKnowledge = List.of("운영체제", "네트워크", "데이터베이스", "자료구조", "알고리즘");
    private final String[] SUPPORTED_DEPARTMENT = {Category.IT.getName(), "개발"};

    @Override
    public InterviewAIRequest configStrategy(AISpecification aiSpec, InterviewSettingDto interviewSettingDto, MessageHistory history) {
        InterviewProgress currentProgress = progressTracker.getCurrentInterviewProgress(interviewSettingDto.getInterviewDetails(), history);
        String rawStrategy = getRawInterviewStrategy(currentProgress.stage());

        List<Message> messageHistory = history.getMessages();
        // TODO: AI에 request 토큰 제한이 있기 때문에 message List에서 필요한 부분만 추출해서 넣어야 함.

        if (currentProgress.stage() == InterviewStage.TECHNICAL) {
            // TODO: 여기서 변경을 가하기 때문에 InterviewSettingDto를 다른 객체로 변경하는 것을 고려해봐야함.
            String selectedSkill = selectSkillBasedOnProgress(currentProgress.progress(), interviewSettingDto.getProfile().getSkills());
            interviewSettingDto.getProfile().setSkills(selectedSkill);
        }

        InterviewSetting setting = interviewSettingCreator.create(aiSpec, interviewSettingDto.getProfile(), rawStrategy);
        return new InterviewAIRequest(messageHistory, setting);
    }

    private String selectSkillBasedOnProgress(double progress, String candidateSkills) {
        List<String> questionTopicList = new ArrayList<>(basicKnowledge);
        if (StringUtils.hasText(candidateSkills)) {
            String[] skills = candidateSkills.trim().split(" ");
            questionTopicList.addAll(Arrays.asList(skills));
        }

        int n = (int) Math.floor(progress * questionTopicList.size());
        return questionTopicList.get(n);
    }

    @Override
    public InterviewAIRequest changeTopic(AISpecification aiSpec, InterviewSettingDto interviewSettingDto, MessageHistory history) {
        InterviewProgress currentProgress = progressTracker.getCurrentInterviewProgress(interviewSettingDto.getInterviewDetails(), history);
        String rawStrategy = getRawInterviewStrategy(currentProgress.stage());
        rawStrategy += interviewConcept.getChangingTopicCommand();

        List<Message> messageHistory = history.getMessages();
        // TODO: AI에 request 토큰 제한이 있기 때문에 message List에서 필요한 부분만 추출해서 넣어야 함.

        if (currentProgress.stage() == InterviewStage.TECHNICAL) {
            // TODO: 여기서 변경을 가하기 때문에 InterviewSettingDto를 다른 객체로 변경하는 것을 고려해봐야함.
            String selectedSkill = selectSkillBasedOnProgress(currentProgress.progress(), interviewSettingDto.getProfile().getSkills());
            interviewSettingDto.getProfile().setSkills(selectedSkill);
        }

        InterviewSetting setting = interviewSettingCreator.create(aiSpec, interviewSettingDto.getProfile(), rawStrategy);
        return new InterviewAIRequest(messageHistory, setting);
    }

    @Override
    public boolean isSupportedDepartment(CandidateProfileForm profile) {
        if(profile == null)
            return false;

        for (String supportedDepartment : SUPPORTED_DEPARTMENT) {
            if(supportedDepartment.equalsIgnoreCase(profile.getDepartment()))
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