package com.mock.interview.interview.application;

import com.mock.interview.interview.domain.exception.InterviewNotFoundException;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.infra.InterviewRepository;
import com.mock.interview.interview.infra.lock.progress.dto.InterviewUserIds;
import com.mock.interview.interview.presentation.dto.InterviewProgressDto;
import com.mock.interview.interviewconversationpair.application.ConversationConvertor;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewconversationpair.infra.InterviewConversationRepositoryForView;
import com.mock.interview.interviewconversationpair.presentation.dto.ConversationContentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InterviewViewService {
    private final InterviewRepository repository;
    private final InterviewConversationRepositoryForView conversationRepositoryForView;

    public InterviewProgressDto findInterview(InterviewUserIds interviewUserIds) {
        Interview interview = InterviewFindingHelper.find(repository, interviewUserIds);
        return new InterviewProgressDto(interview.getId(), interview.getTitle().getTitle(),interview.getExpiredTime());
    }

    // TODO: 페이징 처리 필요.
    public List<ConversationContentDto> findInterviewHistory(InterviewUserIds interviewUserDto) {
        List<InterviewConversationPair> conversations = conversationRepositoryForView
                .findOrderedByCreatedAt(interviewUserDto.interviewId(), interviewUserDto.userId());

        if(conversations.isEmpty())
            throw new InterviewNotFoundException();

        return conversations.stream().map(
                conversationPair -> ConversationConvertor.convert(
                        conversationPair,
                        conversationPair.getQuestion(),
                        conversationPair.getAnswer()
                )
        ).toList();
    }
}
