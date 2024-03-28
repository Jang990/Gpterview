package com.mock.interview.interviewconversationpair.application;

import com.mock.interview.interview.domain.exception.InterviewNotFoundException;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.infra.InterviewRepository;
import com.mock.interview.interviewconversationpair.domain.ChangingTopicService;
import com.mock.interview.interviewconversationpair.domain.exception.InterviewConversationPairNotFoundException;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewconversationpair.infra.InterviewConversationPairRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ConversationPairService {
    private final InterviewRepository interviewRepository;
    private final InterviewConversationPairRepository conversationPairRepository;
    private final ChangingTopicService changingTopicService;

    public void changeQuestionTopic(long loginId, long interviewId, long pairId) {
        Interview interview = interviewRepository.findByIdAndUserId(interviewId, loginId)
                .orElseThrow(InterviewNotFoundException::new);
        InterviewConversationPair conversationPair = conversationPairRepository.findByIdWithInterviewId(pairId, interviewId)
                .orElseThrow(InterviewConversationPairNotFoundException::new);
        changingTopicService.changeTopic(interview, conversationPair);
    }
}
