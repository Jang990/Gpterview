package com.mock.interview.interviewconversationpair.infra;

import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
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
                .innerJoin(interviewConversationPair.interview, interview).fetchJoin()
                .leftJoin(interviewConversationPair.answer, interviewAnswer).fetchJoin()
                .leftJoin(interviewConversationPair.question, interviewQuestion).fetchJoin()
                .where(interviewIdEq(interviewId), userIdEq(userId))
                .orderBy(interviewConversationPair.createdAt.asc())
                .fetch();
    }

    public InterviewConversationPair findConversation(long userId, long interviewId, long conversationId) {
        return query.selectFrom(interviewConversationPair)
                .innerJoin(interviewConversationPair.interview, interview).fetchJoin()
                .leftJoin(interviewConversationPair.answer, interviewAnswer).fetchJoin()
                .leftJoin(interviewConversationPair.question, interviewQuestion).fetchJoin()
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
