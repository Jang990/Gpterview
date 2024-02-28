package com.mock.interview.interviewconversationpair.application;

import com.mock.interview.interview.domain.exception.InterviewNotFoundException;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.infrastructure.InterviewRepository;
import com.mock.interview.interviewconversationpair.domain.ChangingTopicService;
import com.mock.interview.interviewconversationpair.domain.exception.InterviewConversationPairNotFoundException;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewconversationpair.infra.InterviewConversationPairRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConversationPairService {
    private final InterviewRepository interviewRepository;
    private final InterviewConversationPairRepository conversationPairRepository;
    private final ChangingTopicService changingTopicService;

    // TODO: 대화쌍으로 패키지 이동할 것.
    public void changeQuestionTopic(long loginId, long interviewId, long pairId) {
        Interview interview = interviewRepository.findByIdAndUserId(interviewId, loginId)
                .orElseThrow(InterviewNotFoundException::new);
        InterviewConversationPair conversationPair = conversationPairRepository.findByIdWithInterviewId(pairId, interviewId)
                .orElseThrow(InterviewConversationPairNotFoundException::new);
        changingTopicService.changeTopic(interview, conversationPair);
    }
}
