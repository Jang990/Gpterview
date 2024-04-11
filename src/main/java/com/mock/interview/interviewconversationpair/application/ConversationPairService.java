package com.mock.interview.interviewconversationpair.application;

import com.mock.interview.interviewconversationpair.domain.exception.InterviewConversationPairNotFoundException;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewconversationpair.infra.InterviewConversationPairRepository;
import com.mock.interview.interviewquestion.domain.exception.InterviewQuestionNotFoundException;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interviewquestion.infra.InterviewQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ConversationPairService {
    private final InterviewConversationPairRepository conversationPairRepository;
    private final InterviewQuestionRepository questionRepository;

    public void connectQuestion(long loginId, long interviewId, long pairId, long questionId) {
        InterviewConversationPair conversationPair = conversationPairRepository.findWithInterviewUser(pairId, interviewId, loginId)
                .orElseThrow(InterviewConversationPairNotFoundException::new);
        InterviewQuestion question = questionRepository.findOpenQuestion(questionId)
                .orElseThrow(InterviewQuestionNotFoundException::new);

        conversationPair.connectQuestion(question);
    }

    public void recommendAnotherQuestion(long loginId, long interviewId, long pairId) {
        InterviewConversationPair conversationPair = conversationPairRepository.findWithInterviewUser(pairId, interviewId, loginId)
                .orElseThrow(InterviewConversationPairNotFoundException::new);

        conversationPair.restartConversation();
    }
}
