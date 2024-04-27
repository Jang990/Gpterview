package com.mock.interview.interview.infra;

import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewquestion.application.helper.QuestionConvertor;
import com.mock.interview.interviewquestion.presentation.dto.QuestionOverview;
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

import static com.mock.interview.category.domain.model.QJobCategory.jobCategory;
import static com.mock.interview.category.domain.model.QJobPosition.jobPosition;
import static com.mock.interview.interviewconversationpair.domain.model.QInterviewConversationPair.*;
import static com.mock.interview.interviewquestion.domain.model.QInterviewQuestion.interviewQuestion;
import static com.mock.interview.questiontoken.domain.QQuestionTokenization.*;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InterviewQuestionRepositoryForView {
    private final JPAQueryFactory query;

    public Page<QuestionOverview> findOverviewList(long interviewId, Pageable pageable) {
        System.out.println("==쿼리 시작");
        List<InterviewConversationPair> conversationPairList = query
                .selectFrom(interviewConversationPair)
                .innerJoin(interviewConversationPair.question, interviewQuestion).fetchJoin()
                .leftJoin(interviewConversationPair.question.questionToken, questionTokenization).fetchJoin()
                .leftJoin(interviewConversationPair.question.category, jobCategory).fetchJoin()
                .leftJoin(interviewConversationPair.question.position, jobPosition).fetchJoin()
                .where(
                        interviewConversationPair.interview.id.eq(interviewId), isVisible()
                )
                .orderBy(interviewConversationPair.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<QuestionOverview> content = QuestionConvertor.convert(conversationPairList.stream().map(InterviewConversationPair::getQuestion).toList());
        JPAQuery<Long> countQuery = query.select(interviewQuestion.count())
                .from(interviewConversationPair)
                .innerJoin(interviewConversationPair.question, interviewQuestion)
                .where(
                        interviewConversationPair.interview.id.eq(interviewId), isVisible()
                );

        // 현재 페이지의 요소 수가 Limit보다 적으면 Count쿼리를 날리지 않아서 PageImpl보다 좋음
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression isVisible() {
        return interviewConversationPair.question.isHidden.isFalse();
    }

}
