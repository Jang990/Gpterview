package com.mock.interview.interviewquestion.event;


import com.mock.interview.interview.domain.ConversationMessageBroker;
import com.mock.interview.interviewconversationpair.infra.ConversationCacheForAiRequest;
import com.mock.interview.interviewquestion.infra.ai.CustomQuestionCreator;
import com.mock.interview.interviewquestion.infra.ai.dto.Message;
import com.mock.interview.interviewquestion.infra.ai.stage.InterviewProgress;
import com.mock.interview.interviewquestion.infra.ai.stage.InterviewStage;
import com.mock.interview.interview.infrastructure.lock.proceeding.AiResponseProcessingLock;
import com.mock.interview.interview.presentation.dto.message.QuestionInInterviewDto;
import com.mock.interview.interview.domain.exception.InterviewNotFoundException;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.infrastructure.InterviewCacheForAiRequest;
import com.mock.interview.interview.infrastructure.InterviewRepository;
import com.mock.interview.interviewconversationpair.domain.PairStatusChangedToChangingEvent;
import com.mock.interview.interviewconversationpair.domain.exception.InterviewConversationPairNotFoundException;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewconversationpair.infra.InterviewConversationPairRepository;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interviewquestion.infra.InterviewQuestionRepository;
import com.mock.interview.interviewquestion.infra.PublishedQuestionInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class ChangingTopicEventHandler {
    private final CustomQuestionCreator customQuestionCreator;
    private final InterviewRepository interviewRepository;
    private final InterviewConversationPairRepository conversationPairRepository;
    private final InterviewCacheForAiRequest interviewCache;
    private final ConversationCacheForAiRequest conversationCache;
    private final ConversationMessageBroker conversationMessageBroker;
    private final InterviewQuestionRepository questionRepository;

    @Async
    @AiResponseProcessingLock
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(
            classes = PairStatusChangedToChangingEvent.class,
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void getInterviewQuestion(PairStatusChangedToChangingEvent event) {
        InterviewConversationPair conversationPair = conversationPairRepository.findById(event.pairId())
                .orElseThrow(InterviewConversationPairNotFoundException::new);

        // TODO: 밑의 과정을 적절하게 도메인 계층으로 올리기.

        // 요청과정
        long interviewId = conversationPair.getInterview().getId();
        Message message = QuestionRequestHelper.changeTopic(customQuestionCreator, interviewCache, conversationCache, interviewId);

        // Question 저장 과정
        InterviewQuestion question = createQuestion(message, interviewId);
        questionRepository.save(question);

        // pair 수정 과정
        conversationPair.changeTopic(question);

        // 메시지 전송 과정 - TODO: 메시지 브로커를 이벤트 처리 AFTER_COMMIT으로 통일할 것.
        conversationMessageBroker.publish(interviewId,
                new QuestionInInterviewDto(conversationPair.getId(),question.getId(), message.getRole(), message.getContent()));
    }

    private InterviewQuestion createQuestion(Message message, long interviewId) {
        PublishedQuestionInfo publishedQuestion = new PublishedQuestionInfo(
                "GPT", message.getContent(), new InterviewProgress(InterviewStage.TECHNICAL, 0.3)); // TODO: 여기서 변환하지 말고 AI 서비스에서 가져올 것.
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(InterviewNotFoundException::new);
        return InterviewQuestion.createInInterview(questionRepository, interview.getUsers(), interview.getAppliedJob(), publishedQuestion);
    }
}
