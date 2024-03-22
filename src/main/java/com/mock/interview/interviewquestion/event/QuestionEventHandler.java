package com.mock.interview.interviewquestion.event;

import com.mock.interview.interviewanswer.domain.UserAnsweredEvent;
import com.mock.interview.interviewconversationpair.infra.ConversationCacheForAiRequest;
import com.mock.interview.interviewquestion.infra.RecommendedQuestion;
import com.mock.interview.interviewquestion.infra.ai.AiQuestionCreator;
import com.mock.interview.interview.infrastructure.lock.proceeding.AiResponseProcessingLock;
import com.mock.interview.interview.domain.InterviewStartedEvent;
import com.mock.interview.interview.domain.exception.InterviewNotFoundException;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.infrastructure.InterviewCacheForAiRequest;
import com.mock.interview.interview.infrastructure.InterviewRepository;
import com.mock.interview.interviewquestion.domain.CreationQuestionInCustomInterviewService;
import com.mock.interview.interviewquestion.infra.InterviewQuestionRepository;
import com.mock.interview.tech.application.TechSavingHelper;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import com.mock.interview.tech.infrastructure.TechnicalSubjectsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionEventHandler {
    private final AiQuestionCreator aiQuestionCreator;
    private final InterviewRepository interviewRepository;
    private final InterviewCacheForAiRequest interviewCache;
    private final ConversationCacheForAiRequest conversationCache;
    private final InterviewQuestionRepository interviewQuestionRepository;
    private final TechnicalSubjectsRepository technicalSubjectsRepository;
    private final CreationQuestionInCustomInterviewService creationQuestionInCustomInterviewService;

    @Async
    @AiResponseProcessingLock
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(
            classes = InterviewStartedEvent.class,
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void initInterviewConversation(InterviewStartedEvent event) {
        createQuestion(event.interviewId());
    }

    @Async
    @AiResponseProcessingLock
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(
            classes = UserAnsweredEvent.class,
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void getAiResponse(UserAnsweredEvent event) {
        createQuestion(event.interviewId());
    }

    private void createQuestion(long interviewId) {
        RecommendedQuestion question = AiQuestionHelper.createQuestion(aiQuestionCreator, interviewCache, conversationCache, interviewId);
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(InterviewNotFoundException::new);
        List<TechnicalSubjects> techList = TechSavingHelper.saveTechIfNotExist(technicalSubjectsRepository, question.topic());
        creationQuestionInCustomInterviewService.save(interviewQuestionRepository, interview, question, techList);
    }
}
