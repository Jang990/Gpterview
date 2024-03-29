package com.mock.interview.interviewanswer.infra;

import com.mock.interview.interviewanswer.presentation.dto.AnswerForView;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.mock.interview.interviewanswer.domain.model.QInterviewAnswer.*;
import static com.mock.interview.user.domain.model.QUsers.*;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InterviewAnswerRepositoryForView {
    private final JPAQueryFactory query;
    private final int TOP_3 = 3;

    public List<AnswerForView> findAnswerTop3Likes(Long questionIdCond) {
        verifyIdCond(questionIdCond);

        return query.select(
                Projections.constructor(AnswerForView.class,
                        interviewAnswer.id, interviewAnswer.users.username,
                        interviewAnswer.createdAt, interviewAnswer.answer, interviewAnswer.likes))
                .from(interviewAnswer)
                .innerJoin(interviewAnswer.users, users)
                .where(questionIdEq(questionIdCond))
                .orderBy(interviewAnswer.likes.desc(), interviewAnswer.createdAt.desc())
                .limit(TOP_3)
                .fetch();
    }

    public List<AnswerForView> findMyAnswer(Long loginIdCond, Long questionIdCond) {
        verifyIdCond(loginIdCond);
        verifyIdCond(questionIdCond);

        return query.select(
                    Projections.constructor(AnswerForView.class,
                            interviewAnswer.id, interviewAnswer.users.username,
                            interviewAnswer.createdAt, interviewAnswer.answer, interviewAnswer.likes)
                )
                .from(interviewAnswer)
                .innerJoin(interviewAnswer.users, users).on(userIdEq(loginIdCond))
                .where(questionIdEq(questionIdCond))
                .orderBy(interviewAnswer.likes.desc(), interviewAnswer.createdAt.desc())
                .fetch();
    }

    private static void verifyIdCond(Long id) {
        if(id == null)
            throw new IllegalArgumentException();
    }

    private static BooleanExpression questionIdEq(long questionIdCond) {
        return interviewAnswer.question.id.eq(questionIdCond);
    }

    private static BooleanExpression userIdEq(long loginIdCond) {
        return interviewAnswer.users.id.eq(loginIdCond);
    }
}
