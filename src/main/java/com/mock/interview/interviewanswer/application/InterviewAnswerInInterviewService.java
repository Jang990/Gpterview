package com.mock.interview.interviewanswer.application;

import com.mock.interview.interview.infra.InterviewExistsRepository;
import com.mock.interview.interview.infra.lock.progress.InterviewProgressLock;
import com.mock.interview.interview.infra.lock.progress.dto.InterviewConversationIds;
import com.mock.interview.interviewanswer.presentation.dto.InterviewAnswerRequest;
import com.mock.interview.interview.application.helper.InterviewVerificationHelper;
import com.mock.interview.interview.domain.exception.InterviewNotFoundException;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.infra.InterviewRepository;
import com.mock.interview.interviewanswer.domain.InterviewAnswerService;
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
    private final InterviewAnswerService interviewAnswerService;
    private final InterviewExistsRepository interviewExistsRepository;

    @InterviewProgressLock
    public void create(InterviewConversationIds conversationDto, InterviewAnswerRequest answerDto) {
        InterviewVerificationHelper.verify(interviewExistsRepository, conversationDto.interviewId(), conversationDto.userId());
        Interview interview = interviewRepository.findByIdAndUserId(conversationDto.interviewId(), conversationDto.userId())
                .orElseThrow(InterviewNotFoundException::new);
        InterviewConversationPair conversationPair = conversationPairRepository
                .findByIdWithInterviewId(conversationDto.conversationId(), conversationDto.interviewId())
                .orElseThrow(InterviewConversationPairNotFoundException::new);

        interviewAnswerService.saveAnswerInInterview(interviewAnswerRepository, interview, conversationPair, answerDto);
    }
}
