package com.mock.interview.interviewquestion.infra.ai;

import com.mock.interview.category.infra.CategoryModuleFinder;
import com.mock.interview.interview.domain.exception.InterviewNotFoundException;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.infra.InterviewCacheForAiRequest;
import com.mock.interview.interview.infra.InterviewRepository;
import com.mock.interview.interviewconversationpair.infra.ConversationCacheForAiRequest;
import com.mock.interview.interviewquestion.application.QuestionConvertor;
import com.mock.interview.interviewquestion.domain.AiQuestionCreator;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interviewquestion.domain.model.QuestionType;
import com.mock.interview.interviewquestion.infra.InterviewQuestionRepository;
import com.mock.interview.interviewquestion.infra.RecommendedQuestion;
import com.mock.interview.interviewquestion.infra.ai.dto.InterviewInfo;
import com.mock.interview.interviewquestion.infra.ai.dto.Message;
import com.mock.interview.interviewquestion.infra.ai.dto.MessageHistory;
import com.mock.interview.interviewquestion.infra.ai.gpt.AIRequester;
import com.mock.interview.interviewquestion.infra.ai.gpt.InterviewAIRequest;
import com.mock.interview.interviewquestion.infra.ai.progress.InterviewPhase;
import com.mock.interview.interviewquestion.infra.ai.progress.InterviewProgress;
import com.mock.interview.interviewquestion.infra.ai.progress.InterviewProgressTimeBasedTracker;
import com.mock.interview.interviewquestion.infra.ai.prompt.AiPrompt;
import com.mock.interview.interviewquestion.infra.ai.prompt.PromptCreator;
import com.mock.interview.interviewquestion.infra.ai.prompt.configurator.PromptConfiguration;
import com.mock.interview.interviewquestion.infra.ai.prompt.configurator.generator.InterviewPromptConfigurator;
import com.mock.interview.tech.application.TechSavingHelper;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import com.mock.interview.tech.infra.TechnicalSubjectsRepository;
import com.mock.interview.user.domain.model.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AiQuestionCreatorImpl implements AiQuestionCreator {
    private final InterviewCacheForAiRequest interviewCache;
    private final ConversationCacheForAiRequest conversationCache;

    private final InterviewRepository repository;
    private final InterviewQuestionRepository questionRepository;
    private final TechnicalSubjectsRepository technicalSubjectsRepository;

    private final List<InterviewPromptConfigurator> interviewPromptConfiguratorList;
    private final AIRequester requester;
    private final InterviewProgressTimeBasedTracker progressTracker;
    private final PromptCreator promptCreator;

    public InterviewQuestion createQuestion(long interviewId, CreationOption creationOption) {
        RecommendedQuestion recommendedQuestion = create(interviewId, creationOption);
        Interview interview = repository.findById(interviewId)
                .orElseThrow(InterviewNotFoundException::new);
        List<TechnicalSubjects> relatedTechList = TechSavingHelper
                .saveTechIfNotExist(technicalSubjectsRepository, recommendedQuestion.topic());

        InterviewQuestion question = createQuestion(interview, recommendedQuestion);
        question.linkJob(interview.getCategory(), interview.getPosition());
        question.linkTech(relatedTechList);
        return question;
    }

    // TODO: 제거할 것
    public RecommendedQuestion create(long interviewId, CreationOption creationOption) {
        InterviewInfo interviewInfo = interviewCache.findAiInterviewSetting(interviewId);
        MessageHistory history = conversationCache.findCurrentConversation(interviewId);

        InterviewProgress progress = progressTracker.getCurrentInterviewProgress(interviewInfo.config());
        PromptConfiguration promptConfig = createPromptConfig(progress, interviewInfo);
        AiPrompt prompt = createPrompt(promptConfig, creationOption);

        // TODO: AI에 request 토큰 제한이 있기 때문에 message List에서 필요한 부분만 추출해서 넣어야 함.

        Message response = requester.sendRequest(new InterviewAIRequest(history.getMessages(), prompt));
        return createPublishedQuestion(progress, promptConfig, response);
    }

    private AiPrompt createPrompt(PromptConfiguration promptConfig, CreationOption creationOption) {
        return switch (creationOption) {
            case NORMAL -> promptCreator.create(requester, promptConfig);
            case CHANGING_TOPIC -> promptCreator.changeTopic(requester, promptConfig);
        };
    }

    private RecommendedQuestion createPublishedQuestion(InterviewProgress progress, PromptConfiguration promptConfig, Message response) {
        List<String> topic = getTopic(progress.phase(), promptConfig);
        return new RecommendedQuestion(requester.getSignature(), response.getContent(), progress, topic);
    }

    private List<String> getTopic(InterviewPhase phase, PromptConfiguration promptConfig) {
        return switch (phase) {
            case TECHNICAL -> promptConfig.getSkills();
            case EXPERIENCE -> promptConfig.getExperience();
            case PERSONAL -> List.of();
        };
    }

    private PromptConfiguration createPromptConfig(InterviewProgress progress, InterviewInfo interviewInfo) {
        InterviewPromptConfigurator configurator = CategoryModuleFinder
                .findModule(interviewPromptConfiguratorList, interviewInfo.profile().category());
        return configurator.configStrategy(requester, interviewInfo.profile(), progress);
    }

    private InterviewQuestion createQuestion(Interview interview, RecommendedQuestion recommendedQuestion) {
        Users users = interview.getUsers();
        String content = recommendedQuestion.question();
        QuestionType type = QuestionConvertor.convert(recommendedQuestion.progress().phase());
        return InterviewQuestion.create(
                questionRepository, content, users,
                type, recommendedQuestion.createdBy()
        );
    }
}
