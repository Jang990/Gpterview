package com.mock.interview.interview.application;

import com.mock.interview.interview.application.helper.InterviewVerificationHelper;
import com.mock.interview.interview.infra.InterviewExistsRepository;
import com.mock.interview.interview.infra.InterviewRepository;
import com.mock.interview.interviewconversationpair.infra.InterviewConversationPairRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class InterviewDeleteService {
    private final InterviewRepository interviewRepository;
    private final InterviewConversationPairRepository interviewConversationPairRepository;
    private final InterviewExistsRepository interviewExistsRepository;
    public void delete(long interviewId, long userId) {
        InterviewVerificationHelper.verify(interviewExistsRepository, interviewId, userId);
        interviewConversationPairRepository.removeInterview(interviewId);
        interviewRepository.deleteById(interviewId);
    }
}
