package com.mock.interview.conversation.event;

import com.mock.interview.candidate.domain.model.CandidateConfig;
import com.mock.interview.conversation.domain.UserAnsweredEvent;
import com.mock.interview.conversation.domain.model.InterviewConversation;
import com.mock.interview.conversation.infrastructure.InterviewConversationRepository;
import com.mock.interview.conversation.infrastructure.interview.AIService;
import com.mock.interview.conversation.infrastructure.interview.dto.InterviewConfig;
import com.mock.interview.conversation.infrastructure.interview.dto.InterviewInfo;
import com.mock.interview.conversation.infrastructure.interview.dto.InterviewProfile;
import com.mock.interview.conversation.infrastructure.interview.dto.Message;
import com.mock.interview.interview.domain.InterviewStartedEvent;
import com.mock.interview.interview.domain.exception.InterviewNotFoundException;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.infrastructure.InterviewRepository;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConversationEventHandler {
    private final AIService aiService;
    private final InterviewRepository interviewRepository;
    private final InterviewConversationRepository conversationRepository;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW) // TODO: 이 부분 이해가 더 필요함... 이벤트 리스너 트랜잭션이 따로 있는건가?
    @TransactionalEventListener(
            classes = InterviewStartedEvent.class,
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void initInterviewConversation(InterviewStartedEvent event) {
        System.out.println("면접 시작함");
        temporaryCode(event.interviewId());
    }

    private InterviewInfo convert(CandidateConfig config, LocalDateTime interviewExpiredTime) {
        InterviewProfile profile = new InterviewProfile(
                config.getDepartment().getName(),
                config.getAppliedJob().getName(),
                config.getTechSubjects().stream().map(TechnicalSubjects::getName).toList(),
                List.of(config.getExperience())
        );
        InterviewConfig interviewConfig = new InterviewConfig(config.getType(), interviewExpiredTime);
        return new InterviewInfo(profile, interviewConfig);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(
            classes = UserAnsweredEvent.class,
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void getAiResponse(UserAnsweredEvent event) {
        System.out.println("AI 요청 옴");
        // TODO: OpenAI 처리 시간이 2~10초 걸리기 때문에 비동기 처리할 것
//        aiService.getQuestion();
        // TODO: 관련 데이터를 불러올 것. - 몇 분간 지속적으로 사용할 데이터이므로 캐싱하면 좋음

        // 임시 코드
        temporaryCode(event.interviewId());
    }

    private void temporaryCode(long interviewId) {
        Message message = aiService.serviceTemp();
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(InterviewNotFoundException::new);
        InterviewConversation interviewConversation = InterviewConversation.createQuestion(interview, message);
        conversationRepository.save(interviewConversation);
    }
}
