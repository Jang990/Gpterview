package com.mock.interview.interviewquestion.event;

import com.mock.interview.interviewconversationpair.domain.event.AiQuestionRecommendedEvent;
import com.mock.interview.interviewconversationpair.domain.event.StatusChangedToChangingEvent;
import com.mock.interview.interviewconversationpair.domain.exception.InterviewConversationPairNotFoundException;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewconversationpair.infra.InterviewConversationPairRepository;
import com.mock.interview.interviewquestion.domain.AiQuestionCreator;
import com.mock.interview.interviewquestion.infra.RecommendedQuestion;
import com.mock.interview.interview.infra.lock.response.AiResponseAwaitLock;
import com.mock.interview.interview.domain.exception.InterviewNotFoundException;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.infra.InterviewRepository;
import com.mock.interview.interviewquestion.domain.AiQuestionCreationService;
import com.mock.interview.interviewquestion.infra.InterviewQuestionRepository;
import com.mock.interview.tech.application.TechSavingHelper;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import com.mock.interview.tech.infra.TechnicalSubjectsRepository;
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
public class AiQuestionEventHandler {
    private final AiQuestionCreator aiQuestionCreator;
    private final InterviewRepository interviewRepository;
    private final InterviewQuestionRepository interviewQuestionRepository;
    private final TechnicalSubjectsRepository technicalSubjectsRepository;
    private final AiQuestionCreationService aiQuestionCreationService;
    private final InterviewConversationPairRepository conversationPairRepository;

    @Async
    @AiResponseAwaitLock
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(
            classes = AiQuestionRecommendedEvent.class,
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void handle(AiQuestionRecommendedEvent event) {
        long interviewId = event.interviewId();
        RecommendedQuestion question = aiQuestionCreator.create(interviewId, AiQuestionCreator.CreationOption.NORMAL);
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(InterviewNotFoundException::new);
        List<TechnicalSubjects> techList = TechSavingHelper.saveTechIfNotExist(technicalSubjectsRepository, question.topic());

        aiQuestionCreationService.save(interviewQuestionRepository, event.pairId(), interview, question, techList);
    }

    @Async
    @AiResponseAwaitLock
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(
            classes = StatusChangedToChangingEvent.class,
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void handleChangingTopicRequest(StatusChangedToChangingEvent event) {
        long interviewId = event.interviewId();
        InterviewConversationPair conversationPair = conversationPairRepository.findById(event.pairId())
                .orElseThrow(InterviewConversationPairNotFoundException::new);

        RecommendedQuestion recommendedQuestion = aiQuestionCreator.create(event.interviewId(), AiQuestionCreator.CreationOption.CHANGING_TOPIC);
        saveQuestion(interviewId, recommendedQuestion, conversationPair);
    }

    private void saveQuestion(long interviewId, RecommendedQuestion recommendedQuestion, InterviewConversationPair conversationPair) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(InterviewNotFoundException::new);
        List<TechnicalSubjects> techList = TechSavingHelper.saveTechIfNotExist(technicalSubjectsRepository, recommendedQuestion.topic());
        aiQuestionCreationService.saveTopicChangedQuestion(interviewQuestionRepository, interview, conversationPair, recommendedQuestion, techList);
    }
}
