package com.mock.interview.interviewquestion.event;

import com.mock.interview.interview.domain.exception.InterviewNotFoundException;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.infra.InterviewRepository;
import com.mock.interview.interview.infra.cache.InterviewCacheRepository;
import com.mock.interview.interviewconversationpair.domain.event.ConversationStartedEvent;
import com.mock.interview.interviewconversationpair.domain.exception.InterviewConversationPairNotFoundException;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewconversationpair.infra.InterviewConversationPairRepository;
import com.mock.interview.interviewquestion.domain.AiQuestionCreator;
import com.mock.interview.interviewquestion.domain.ConversationQuestionService;
import com.mock.interview.interviewquestion.domain.QuestionRecommender;
import com.mock.interview.interviewquestion.infra.InterviewQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class ConversationStartedEventHandler {
    private final InterviewRepository interviewRepository;
    private final InterviewQuestionRepository questionRepository;
    private final InterviewConversationPairRepository conversationPairRepository;
    private final InterviewCacheRepository interviewCacheRepository;

    private final ConversationQuestionService conversationQuestionService;
    private final AiQuestionCreator aiQuestionCreator;
    private final QuestionRecommender questionRecommender;


    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(
            classes = ConversationStartedEvent.class,
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void handle(ConversationStartedEvent event) {
        Interview interview = interviewRepository.findById(event.interviewId())
                .orElseThrow(InterviewNotFoundException::new);
        InterviewConversationPair conversationPair = conversationPairRepository.findById(event.pairId())
                .orElseThrow(InterviewConversationPairNotFoundException::new);

        Long relatedCategoryQuestionSize = questionRepository.countCategoryQuestion(interview.getCategory().getName());

        try {
            conversationQuestionService.chooseMethod(
                    aiQuestionCreator, questionRecommender, relatedCategoryQuestionSize,
                    event.interviewId(), conversationPair
            );
        } catch (Throwable throwable) {
            interviewCacheRepository.expireInterviewInfo(event.interviewId());
        }
    }
}
