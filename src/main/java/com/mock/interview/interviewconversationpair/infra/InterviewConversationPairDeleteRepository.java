package com.mock.interview.interviewconversationpair.infra;

import com.mock.interview.interviewconversationpair.domain.model.QInterviewConversationPair;
import com.mock.interview.interviewquestion.domain.model.QInterviewQuestion;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.mock.interview.interviewconversationpair.domain.model.QInterviewConversationPair.*;
import static com.mock.interview.interviewquestion.domain.model.QInterviewQuestion.*;

@Repository
@Transactional
@RequiredArgsConstructor
public class InterviewConversationPairDeleteRepository {
    private final JPAQueryFactory query;
    public long removeExperienceQuestion(long experienceId) {
        return query.update(interviewConversationPair)
                .set(interviewConversationPair.question, Expressions.nullExpression())
                .where(
                        interviewConversationPair.question.id.in(
                                JPAExpressions
                                        .select(interviewQuestion.id)
                                        .from(interviewQuestion)
                                        .where(interviewQuestion.experience.id.eq(experienceId))
                        )
                )
                .execute();
    }
}
