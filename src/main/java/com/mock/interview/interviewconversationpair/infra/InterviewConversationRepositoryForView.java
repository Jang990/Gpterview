package com.mock.interview.interviewconversationpair.infra;


import com.mock.interview.interview.presentation.dto.InterviewRole;
import com.mock.interview.interview.presentation.dto.message.MessageDto;
import com.mock.interview.interviewconversationpair.presentation.dto.InterviewConversationPairDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

import static com.mock.interview.interview.domain.model.QInterview.*;
import static com.mock.interview.interviewanswer.domain.model.QInterviewAnswer.*;
import static com.mock.interview.interviewconversationpair.domain.model.QInterviewConversationPair.*;
import static com.mock.interview.interviewquestion.domain.model.QInterviewQuestion.*;

@Repository
@RequiredArgsConstructor
public class InterviewConversationRepositoryForView {
    private final JPAQueryFactory query;

    public Slice<InterviewConversationPairDto> findInterviewConversations(long interviewId, long userId, Pageable pageable) {
        List<InterviewConversationPairDto> result = query.select(
                        Projections.constructor(InterviewConversationPairDto.class,
                                interviewConversationPair.id,
                                Projections.constructor(MessageDto.class,
                                        interviewConversationPair.question.id,
                                        Expressions.constant(InterviewRole.AI),
                                        interviewConversationPair.question.question
                                ),
                                Projections.constructor(MessageDto.class,
                                        interviewConversationPair.answer.id,
                                        Expressions.constant(InterviewRole.USER),
                                        interviewConversationPair.answer.answer
                                )
                        )
                )
                .from(interviewConversationPair)
                .innerJoin(interviewConversationPair.interview, interview).on(interview.users.id.eq(userId))
                .innerJoin(interviewConversationPair.question, interviewQuestion)
                .leftJoin(interviewConversationPair.answer, interviewAnswer)
                .where(interviewConversationPair.interview.id.eq(interviewId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .orderBy(interviewConversationPair.createdAt.desc())
                .fetch();

        Collections.reverse(result);

        if(result.size() > pageable.getPageSize())
            return new SliceImpl<>(result.subList(0, pageable.getPageSize()), pageable, true);

        return new SliceImpl<>(result, pageable, false);
    }
}
