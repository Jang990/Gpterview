package com.mock.interview.interviewconversationpair.infra;


import com.mock.interview.interview.presentation.dto.InterviewRole;
import com.mock.interview.interview.presentation.dto.message.MessageDto;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewconversationpair.presentation.dto.ConversationContentDto;
import com.mock.interview.interviewconversationpair.presentation.dto.InterviewConversationPairDto;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.mock.interview.interview.domain.model.QInterview.*;
import static com.mock.interview.interviewanswer.domain.model.QInterviewAnswer.*;
import static com.mock.interview.interviewconversationpair.domain.model.QInterviewConversationPair.*;
import static com.mock.interview.interviewquestion.domain.model.QInterviewQuestion.*;

@Repository
@RequiredArgsConstructor
public class InterviewConversationRepositoryForView {
    private final JPAQueryFactory query;

    public List<InterviewConversationPair> findOrderedByCreatedAt(long interviewId, long userId) {
        return query.selectFrom(interviewConversationPair)
                .innerJoin(interviewConversationPair.interview, interview).on(interview.users.id.eq(userId))
                .leftJoin(interviewConversationPair.question, interviewQuestion)
                .leftJoin(interviewConversationPair.answer, interviewAnswer)
                .where(interviewIdEq(interviewId))
                .orderBy(interviewConversationPair.createdAt.asc())
                .fetch();
    }

    @NotNull
    private static ConstructorExpression<ConversationContentDto> generateConversationContentDto() {
        return Projections.constructor(ConversationContentDto.class,
                Projections.constructor(InterviewConversationPairDto.class,
                        interviewConversationPair.id,
                        interviewConversationPair.status
                ),
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
        );
    }

    public InterviewConversationPair findConversation(long userId, long interviewId, long conversationId) {
        return query.selectFrom(interviewConversationPair)
                .innerJoin(interviewConversationPair.interview, interview).on(interview.users.id.eq(userId))
                .leftJoin(interviewConversationPair.question, interviewQuestion)
                .leftJoin(interviewConversationPair.answer, interviewAnswer)
                .where(conversationIdEq(conversationId), interviewIdEq(interviewId), userIdEq(userId))
                .orderBy(interviewConversationPair.createdAt.desc())
                .fetchOne();
    }

    private BooleanExpression userIdEq(long userId) {
        return interviewConversationPair.interview.users.id.eq(userId);
    }

    private BooleanExpression conversationIdEq(long conversationId) {
        return interviewConversationPair.id.eq(conversationId);
    }

    private static BooleanExpression interviewIdEq(long interviewId) {
        return interviewConversationPair.interview.id.eq(interviewId);
    }
}
