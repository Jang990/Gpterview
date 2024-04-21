package com.mock.interview.interviewconversationpair.application.helper;

import com.mock.interview.global.RepositoryConst;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewconversationpair.infra.InterviewConversationPairRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public final class LastConversationHelper {
    private static final int FIRST_IDX = 0;

    private LastConversationHelper() {}

    public static Optional<InterviewConversationPair> findCurrentCompletedConversation(InterviewConversationPairRepository repository, long interviewId) {
        List<InterviewConversationPair> result = repository
                .findLastCompletedConversation(interviewId, RepositoryConst.LIMIT_ONE);

        if(result.isEmpty()) // 이제 대화 시작
            return Optional.empty();
        return Optional.of(result.get(FIRST_IDX)); // 이전 대화 존재
    }
}
