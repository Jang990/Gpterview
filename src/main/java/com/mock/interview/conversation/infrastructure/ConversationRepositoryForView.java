package com.mock.interview.conversation.infrastructure;

import com.mock.interview.conversation.domain.model.InterviewConversationType;
import com.mock.interview.conversation.presentation.dto.MessageDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

import static com.mock.interview.conversation.domain.model.QInterviewConversation.*;
import static com.mock.interview.interview.domain.model.QInterview.*;

@Repository
@RequiredArgsConstructor
public class ConversationRepositoryForView {
    private final JPAQueryFactory query;

    public Slice<MessageDto> findInterviewConversations(long interviewId, long userId, Pageable pageable) {
        List<MessageDto> result = query.select(
                        Projections.constructor(MessageDto.class,
                                interviewConversation.interviewConversationType.stringValue(),
                                interviewConversation.content
                        )
                )
                .from(interviewConversation)
                .join(interviewConversation.interview, interview)
                .where(
                        interviewConversation.interview.id.eq(interviewId)
                        .and(interviewConversation.interview.users.id.eq(userId))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .orderBy(interviewConversation.createdAt.asc())
                .fetch();

        Collections.reverse(result);

        if(result.size() > pageable.getPageSize())
            return new SliceImpl<>(result.subList(0, pageable.getPageSize()), pageable, true);

        return new SliceImpl<>(result, pageable, false);
    }

}
