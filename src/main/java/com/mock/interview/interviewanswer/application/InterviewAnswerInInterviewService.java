package com.mock.interview.interviewanswer.application;

import com.mock.interview.interview.presentation.dto.message.MessageDto;
import com.mock.interview.interview.application.InterviewVerificationHelper;
import com.mock.interview.interview.domain.exception.InterviewNotFoundException;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.infrastructure.InterviewRepository;
import com.mock.interview.interviewanswer.domain.AnswerInRunningInterviewService;
import com.mock.interview.interviewanswer.infra.InterviewAnswerRepository;
import com.mock.interview.interviewconversationpair.domain.exception.InterviewConversationPairNotFoundException;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewconversationpair.infra.InterviewConversationPairRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class InterviewAnswerInInterviewService {
    private final InterviewRepository interviewRepository;
    private final InterviewConversationPairRepository conversationPairRepository;
    private final InterviewAnswerRepository interviewAnswerRepository;
    private final AnswerInRunningInterviewService answerInRunningInterviewService;

    public void create(long loginId, long interviewId, long pairId, MessageDto answerDto) {
        InterviewVerificationHelper.verify(interviewRepository, interviewId, loginId);
        Interview interview = interviewRepository.findByIdAndUserId(interviewId, loginId)
                .orElseThrow(InterviewNotFoundException::new);
        InterviewConversationPair conversationPair = conversationPairRepository.findByIdWithInterviewId(pairId, interviewId)
                .orElseThrow(InterviewConversationPairNotFoundException::new);

        answerInRunningInterviewService.saveAnswerInInterview(interviewAnswerRepository, interview, conversationPair, answerDto);
    }
}
