package com.mock.interview.interviewquestion.infra;

import com.mock.interview.interviewquestion.application.helper.QuestionConvertor;
import com.mock.interview.interviewquestion.domain.exception.InterviewQuestionNotFoundException;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interviewquestion.domain.model.QInterviewQuestion;
import com.mock.interview.interviewquestion.presentation.dto.*;
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

import static com.mock.interview.category.domain.model.QJobCategory.*;
import static com.mock.interview.category.domain.model.QJobPosition.*;
import static com.mock.interview.interviewquestion.domain.model.QInterviewQuestion.*;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionRepositoryForView {
    private final JPAQueryFactory query;
    private final int TOP_3 = 3;

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

    public List<ChildQuestionOverview> findChildQuestionTop3Likes(Long questionIdCond) {
        return query.select(
                        Projections.constructor(ChildQuestionOverview.class,
                                interviewQuestion.id, interviewQuestion.createdBy, interviewQuestion.createdAt,
                                interviewQuestion.question, interviewQuestion.likes)
                )
                .from(interviewQuestion)
                .where(findChildQuestion(questionIdCond), isVisible())
                .orderBy(interviewQuestion.likes.desc(), interviewQuestion.createdAt.desc())
                .limit(TOP_3)
                .fetch();
    }

    public Page<QuestionOverview> findOverviewList(
            QuestionSearchOptionsDto searchOptions,
            Pageable pageable
    ) {
        if(searchOptions == null)
            searchOptions = new QuestionSearchOptionsDto();
        if(searchOptions.getSearchCond() == null)
            searchOptions.setSearchCond(new QuestionSearchCond());

        List<InterviewQuestion> questions = query.selectFrom(interviewQuestion)
                .leftJoin(interviewQuestion.category, jobCategory).fetchJoin()
                .leftJoin(interviewQuestion.position, jobPosition).fetchJoin()
                .where(
                        findChildQuestion(searchOptions.getParentQuestionIdCond()),
                        categoryEq(searchOptions.getCategoryNameCond()),
                        positionEq(searchOptions.getPositionNameCond()), createdByEq(searchOptions.getCreatedByCond()),
                        questionTypeEq(searchOptions.getSearchCond().getTypeCond()),
                        keywordContains(searchOptions.getSearchCond().getKeywordCond()),
                        isVisible()
                )
                .orderBy(interviewQuestion.likes.desc(), interviewQuestion.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<QuestionOverview> content = QuestionConvertor.convert(questions);
        JPAQuery<Long> countQuery = query.select(interviewQuestion.count())
                .from(interviewQuestion)
                .where(
                        findChildQuestion(searchOptions.getParentQuestionIdCond()),
                        categoryEq(searchOptions.getCategoryNameCond()),
                        positionEq(searchOptions.getPositionNameCond()),
                        createdByEq(searchOptions.getCreatedByCond()),
                        questionTypeEq(searchOptions.getSearchCond().getTypeCond()),
                        keywordContains(searchOptions.getSearchCond().getKeywordCond()),
                        isVisible()
                );

        // 현재 페이지의 요소 수가 Limit보다 적으면 Count쿼리를 날리지 않아서 PageImpl보다 좋음
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression keywordContains(String keywordCond) {
        return keywordCond == null ?
                null : interviewQuestion.question.contains(keywordCond);
    }

    private BooleanExpression questionTypeEq(QuestionTypeForView typeCond) {
        return typeCond == null ?
                null : interviewQuestion.questionType.eq(QuestionConvertor.convert(typeCond));
    }

    private BooleanExpression findChildQuestion(Long parentQuestionIdCond) {
        return parentQuestionIdCond == null ?
                null : interviewQuestion.parentQuestion.id.eq(parentQuestionIdCond);
    }

    private BooleanExpression categoryEq(String jobCategoryCond) {
        return jobCategoryCond == null ? null : interviewQuestion.category.name.eq(jobCategoryCond);
    }

    private BooleanExpression positionEq(String jobPositionCond) {
        return jobPositionCond == null ? null : interviewQuestion.position.name.eq(jobPositionCond);
    }

    private BooleanExpression createdByEq(String createdBy) {
        return createdBy == null ? null : interviewQuestion.createdBy.eq(createdBy);
    }

    private BooleanExpression questionIdEq(Long questionIdCond) {
        return questionIdCond == null ? null : interviewQuestion.id.eq(questionIdCond);
    }

    private BooleanExpression ownerIdEq(Long ownerId) {
        return ownerId == null ? null : interviewQuestion.owner.id.eq(ownerId);
    }

    private BooleanExpression isVisible() {
        return interviewQuestion.isHidden.isFalse();
    }
}
