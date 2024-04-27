package com.mock.interview.interviewconversationpair.infra;

import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.questiontoken.domain.QQuestionTokenization;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.mock.interview.interview.domain.model.QInterview.*;
import static com.mock.interview.interviewanswer.domain.model.QInterviewAnswer.*;
import static com.mock.interview.interviewconversationpair.domain.model.QInterviewConversationPair.*;
import static com.mock.interview.interviewquestion.domain.model.QInterviewQuestion.*;
import static com.mock.interview.questiontoken.domain.QQuestionTokenization.*;

@Repository
@RequiredArgsConstructor
public class InterviewConversationRepositoryForView {
    private final JPAQueryFactory query;

    public List<InterviewConversationPair> findOrderedByCreatedAt(long interviewId, long userId) {
        return query.selectFrom(interviewConversationPair)
                .innerJoin(interviewConversationPair.interview, interview).on(interview.users.id.eq(userId)).fetchJoin()
                .leftJoin(interviewConversationPair.question, interviewQuestion).fetchJoin()
                .leftJoin(interviewConversationPair.question.questionToken, questionTokenization).fetchJoin()
                .leftJoin(interviewConversationPair.answer, interviewAnswer).fetchJoin()
                .where(interviewIdEq(interviewId))
                .orderBy(interviewConversationPair.createdAt.asc())
                .fetch();
    }

    public InterviewConversationPair findConversation(long userId, long interviewId, long conversationId) {
        return query.selectFrom(interviewConversationPair)
                .innerJoin(interviewConversationPair.interview, interview).on(interview.users.id.eq(userId)).fetchJoin()
                .leftJoin(interviewConversationPair.question, interviewQuestion).fetchJoin()
                .leftJoin(interviewConversationPair.question.questionToken, questionTokenization).fetchJoin()
                .leftJoin(interviewConversationPair.answer, interviewAnswer).fetchJoin()
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
