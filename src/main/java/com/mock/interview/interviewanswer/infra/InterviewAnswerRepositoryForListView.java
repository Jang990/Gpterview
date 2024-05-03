package com.mock.interview.interviewanswer.infra;

import com.mock.interview.interviewanswer.presentation.dto.AnswerDetailDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.mock.interview.interviewanswer.domain.model.QInterviewAnswer.*;
import static com.mock.interview.interviewquestion.domain.model.QInterviewQuestion.interviewQuestion;
import static com.mock.interview.user.domain.model.QUsers.*;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InterviewAnswerRepositoryForListView {
    private final JPAQueryFactory query;
    private final int TOP_3 = 3;

    public List<AnswerDetailDto> findAnswerTop3Likes(long questionIdCond) {
        return query.select(
                Projections.constructor(AnswerDetailDto.class,
                        interviewAnswer.id, interviewAnswer.users.username,
                        interviewAnswer.createdAt, interviewAnswer.answer, interviewAnswer.likes))
                .from(interviewAnswer)
                .innerJoin(interviewAnswer.users, users)
                .where(questionIdEq(questionIdCond))
                .orderBy(interviewAnswer.likes.desc(), interviewAnswer.createdAt.desc())
                .limit(TOP_3)
                .fetch();
    }

    public List<AnswerDetailDto> findMyAnswer(long loginIdCond, long questionIdCond) {
        return query.select(
                    Projections.constructor(AnswerDetailDto.class,
                            interviewAnswer.id, interviewAnswer.users.username,
                            interviewAnswer.createdAt, interviewAnswer.answer, interviewAnswer.likes)
                )
                .from(interviewAnswer)
                .innerJoin(interviewAnswer.users, users).on(userIdEq(loginIdCond))
                .where(questionIdEq(questionIdCond))
                .orderBy(interviewAnswer.likes.desc(), interviewAnswer.createdAt.desc())
                .fetch();
    }

    public Page<AnswerDetailDto> findQuestionAnswerPage(long questionId, Pageable pageable) {
        List<AnswerDetailDto> content = query.select(
                        Projections.constructor(AnswerDetailDto.class,
                                interviewAnswer.id, interviewAnswer.users.username,
                                interviewAnswer.createdAt, interviewAnswer.answer, interviewAnswer.likes)
                )
                .from(interviewAnswer)
                .innerJoin(interviewAnswer.users, users)
                .where(questionIdEq(questionId))
                .orderBy(interviewAnswer.likes.desc(), interviewAnswer.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = query.select(interviewQuestion.count())
                .from(interviewAnswer)
                .innerJoin(interviewAnswer.users, users)
                .where(questionIdEq(questionId));

        // 현재 페이지의 요소 수가 Limit보다 적으면 Count쿼리를 날리지 않아서 PageImpl보다 좋음
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    public AnswerDetailDto findAnswer(long answerId, long questionId) {
        return query.select(
                        Projections.constructor(AnswerDetailDto.class,
                                interviewAnswer.id, interviewAnswer.users.username,
                                interviewAnswer.createdAt, interviewAnswer.answer, interviewAnswer.likes
                        )
                )
                .from(interviewAnswer)
                .innerJoin(interviewAnswer.users, users)
                .where(answerIdEq(answerId), questionIdEq(questionId))
                .fetchOne();
    }

    private static BooleanExpression answerIdEq(Long answerId) {
        return answerId == null ? null : interviewAnswer.id.eq(answerId);
    }

    private static BooleanExpression questionIdEq(Long questionIdCond) {
        return questionIdCond == null ? null : interviewAnswer.question.id.eq(questionIdCond);
    }

    private static BooleanExpression userIdEq(long loginIdCond) {
        return interviewAnswer.users.id.eq(loginIdCond);
    }
}
