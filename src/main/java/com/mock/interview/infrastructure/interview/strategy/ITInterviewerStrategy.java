package com.mock.interview.infrastructure.interview.strategy;

import com.mock.interview.domain.Category;
import com.mock.interview.infrastructure.interview.dto.MessageHistory;
import com.mock.interview.infrastructure.interview.gpt.AISpecification;
import com.mock.interview.infrastructure.interview.gpt.InterviewAIRequest;
import com.mock.interview.infrastructure.interview.setting.InterviewSetting;
import com.mock.interview.infrastructure.interview.setting.InterviewSettingCreator;
import com.mock.interview.presentaion.dto.InterviewInfoDto;
import com.mock.interview.infrastructure.interview.dto.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ITInterviewerStrategy implements InterviewerStrategy {

    private final ITInterviewConcept interviewConcept;
    private final InterviewSettingCreator interviewSettingCreator;
    private final Category[] SUPPORTED_CATEGORY = {Category.IT};

    @Override
    public InterviewAIRequest configStrategy(AISpecification aiSpec, InterviewInfoDto interviewInfoDto, MessageHistory history) {
        // TODO: 임시 코드를 적절한 로직으로 수정할 것
        int cnt = history.getMessages().size();
        if (cnt <= 6) {
            return createRequestForTechnical(aiSpec, interviewInfoDto, history);
        } else if (cnt <= 12) {
            return createRequestForProjectExperience(aiSpec, interviewInfoDto, history);
        } else {
            return createRequestForPersonal(aiSpec, interviewInfoDto, history);
        }
    }

    @Override
    public boolean isSupportedCategory(Category category) {
        for (Category supportedCategory : SUPPORTED_CATEGORY) {
            if(supportedCategory == category)
                return true;
        }
        
        return false;
    }

    private InterviewAIRequest createRequestForPersonal(AISpecification aiSpec, InterviewInfoDto interviewInfoDto, MessageHistory history) {
        System.out.println("인성 면접 전략 실행");
        String personalSetting = interviewConcept.getPersonal();
        List<Message> messageHistory = history.getMessages();

        // TODO: AI에 request 토큰 제한이 있기 때문에 message List에서 필요한 부분만 추출해서 넣어야 함.
        List<Message> requestMessages = messageHistory;

        InterviewSetting setting = interviewSettingCreator.create(aiSpec, interviewInfoDto.getProfile(), personalSetting);
        return new InterviewAIRequest(requestMessages, setting);
    }

    private InterviewAIRequest createRequestForProjectExperience(AISpecification aiSpec, InterviewInfoDto interviewInfoDto, MessageHistory history) {
        System.out.println("경험 면접 전략 실행");
        String experienceSetting = interviewConcept.getExperience();
        List<Message> messageHistory = history.getMessages();

        // TODO: AI에 request 토큰 제한이 있기 때문에 message List에서 필요한 부분만 추출해서 넣어야 함.
        List<Message> requestMessages = messageHistory;

        InterviewSetting setting = interviewSettingCreator.create(aiSpec, interviewInfoDto.getProfile(), experienceSetting);
        return new InterviewAIRequest(requestMessages, setting);
    }

    private InterviewAIRequest createRequestForTechnical(AISpecification aiSpec, InterviewInfoDto interviewInfoDto, MessageHistory history) {
        System.out.println("기술 면접 전략 실행");
        String technicalSetting = interviewConcept.getTechnical();

        // TODO: AI에 request 토큰 제한이 있기 때문에 message List에서 필요한 부분만 추출해서 넣어야 함.
        List<Message> messageHistory = history.getMessages();
        // aiSpec.getMaxToken();
        InterviewSetting setting = interviewSettingCreator.create(aiSpec, interviewInfoDto.getProfile(), technicalSetting);

        return new InterviewAIRequest(messageHistory, setting);
    }
}