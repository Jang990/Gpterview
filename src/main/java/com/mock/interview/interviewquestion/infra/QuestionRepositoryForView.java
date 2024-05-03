package com.mock.interview.interviewquestion.infra;

import com.mock.interview.interviewquestion.application.helper.QuestionConvertor;
import com.mock.interview.interviewquestion.domain.exception.InterviewQuestionNotFoundException;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interviewquestion.domain.model.QInterviewQuestion;
import com.mock.interview.interviewquestion.presentation.dto.*;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.mock.interview.category.domain.model.QJobCategory.*;
import static com.mock.interview.category.domain.model.QJobPosition.*;
import static com.mock.interview.interviewquestion.domain.model.QInterviewQuestion.*;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionRepositoryForView {
    private final JPAQueryFactory query;

    public ParentQuestionSummaryDto findParentQuestionSummary(long parentQuestionId) {
        return query.select(
                    Projections.constructor(ParentQuestionSummaryDto.class,
                            interviewQuestion.id, interviewQuestion.question,
                            interviewQuestion.owner.id, interviewQuestion.isHidden
                    )
                )
                .from(interviewQuestion)
                .where(interviewQuestion.id.eq(parentQuestionId)).fetchOne();
    }

    public ParentQuestionSummaryDto findQuestionSummary(long questionId) {
        return query.select(
                        Projections.constructor(ParentQuestionSummaryDto.class,
                                interviewQuestion.id, interviewQuestion.question, interviewQuestion.owner.id, interviewQuestion.isHidden
                        )
                )
                .from(interviewQuestion)
                .where(interviewQuestion.id.eq(questionId)).fetchOne();
    }

    public QuestionForm findQuestionForm(long questionIdCond, long userId) {
        QInterviewQuestion parent = new QInterviewQuestion("parent");
        InterviewQuestion question = query.selectFrom(interviewQuestion)
                .leftJoin(interviewQuestion.category, jobCategory).fetchJoin()
                .leftJoin(interviewQuestion.position, jobPosition).fetchJoin()
                .leftJoin(interviewQuestion.parentQuestion, parent).fetchJoin()
                .where(questionIdEq(questionIdCond), ownerIdEq(userId))
                .fetchOne();

        if(question == null)
            throw new InterviewQuestionNotFoundException();

        return QuestionConvertor.convertForm(question);
    }

    /** isHidden여부와 상관없이 가져오므로 권한에 따라 redirect 필요 */
    public QuestionOverview findQuestion(Long questionIdCond) {
        QInterviewQuestion parent = new QInterviewQuestion("parent");
        InterviewQuestion question = query.selectFrom(interviewQuestion)
                .leftJoin(interviewQuestion.category, jobCategory).fetchJoin()
                .leftJoin(interviewQuestion.position, jobPosition).fetchJoin()
                .leftJoin(interviewQuestion.parentQuestion, parent).fetchJoin()
                .where(questionIdEq(questionIdCond))
                .fetchOne();

        if(question == null)
            throw new InterviewQuestionNotFoundException();

        return QuestionConvertor.convert(question);
    }


    private BooleanExpression questionIdEq(Long questionIdCond) {
        return questionIdCond == null ? null : interviewQuestion.id.eq(questionIdCond);
    }

    private BooleanExpression ownerIdEq(Long ownerId) {
        return ownerId == null ? null : interviewQuestion.owner.id.eq(ownerId);
    }
}
