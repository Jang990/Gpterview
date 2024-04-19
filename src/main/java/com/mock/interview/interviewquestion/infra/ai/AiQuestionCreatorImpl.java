package com.mock.interview.interviewquestion.infra.ai;

import com.mock.interview.interview.domain.exception.InterviewNotFoundException;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.infra.InterviewRepository;
import com.mock.interview.interview.infra.cache.InterviewCacheRepository;
import com.mock.interview.interview.infra.cache.dto.InterviewInfo;
import com.mock.interview.interview.infra.progress.InterviewProgressTracker;
import com.mock.interview.interview.infra.progress.dto.InterviewProgress;
import com.mock.interview.interview.infra.prompt.InterviewPromptCreationService;
import com.mock.interview.interviewconversationpair.infra.ConversationCacheForAiRequest;
import com.mock.interview.interviewquestion.application.QuestionConvertor;
import com.mock.interview.interviewquestion.domain.AiQuestionCreator;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interviewquestion.domain.model.QuestionType;
import com.mock.interview.interviewquestion.infra.InterviewQuestionRepository;
import com.mock.interview.interviewquestion.infra.ai.dto.MessageHistory;
import com.mock.interview.interviewquestion.infra.ai.gpt.AIRequester;
import com.mock.interview.interviewquestion.infra.ai.gpt.InterviewAIRequest;
import com.mock.interview.interviewquestion.infra.ai.prompt.AiPrompt;
import com.mock.interview.tech.application.TechSavingHelper;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import com.mock.interview.tech.infra.TechnicalSubjectsRepository;
import com.mock.interview.user.domain.model.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiQuestionCreatorImpl implements AiQuestionCreator {
    private final ConversationCacheForAiRequest conversationCache;
    private final InterviewCacheRepository interviewCache;

    private final InterviewRepository repository;
    private final InterviewQuestionRepository questionRepository;
    private final TechnicalSubjectsRepository technicalSubjectsRepository;

    private final AIRequester requester;
    private final InterviewProgressTracker tracker;
    private final InterviewPromptCreationService promptCreationService;

    @Override
    public InterviewQuestion create(long interviewId, CreationOption creationOption) {
        InterviewInfo interviewInfo = interviewCache.findProgressingInterviewInfo(interviewId);
        InterviewProgress interviewProgress = tracker.trace(interviewInfo);

        String aiQuestion = requestAiServer(interviewInfo, interviewProgress, creationOption);
        return createQuestion(interviewId, interviewProgress, aiQuestion);
    }

    private String requestAiServer(InterviewInfo interviewInfo, InterviewProgress interviewProgress, CreationOption creationOption) {
        MessageHistory history = conversationCache.findCurrentConversation(interviewInfo.interviewId());
        AiPrompt prompt = promptCreationService.create(interviewInfo, interviewProgress, creationOption);
        // TODO: AI에 request 토큰 제한이 있기 때문에 message List에서 필요한 부분만 추출해서 넣어야 함.
        return requester.sendRequest(new InterviewAIRequest(history.getMessages(), prompt)).getContent();
    }

    private InterviewQuestion createQuestion(long interviewId, InterviewProgress progress, String aiQuestion) {
        Interview interview = repository.findById(interviewId)
                .orElseThrow(InterviewNotFoundException::new);
        Users users = interview.getUsers();
        QuestionType type = QuestionConvertor.convert(progress.phase());

        // TODO: 기술, 경험, 인성 등등 여러 페이즈를 지원해야함.
        TechnicalSubjects relatedTech = TechSavingHelper
                .saveTechIfNotExist(technicalSubjectsRepository, progress.getTopicContent());

        InterviewQuestion question = InterviewQuestion.create(
                questionRepository, aiQuestion, users,
                type, requester.getSignature()
        );
        question.linkJob(interview.getCategory(), interview.getPosition());
        question.linkTech(relatedTech);
        return questionRepository.save(question);
    }
}
