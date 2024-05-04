package com.mock.interview.interviewquestion.infra;

import com.mock.interview.interviewquestion.application.helper.QuestionConvertor;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
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

import static com.mock.interview.category.domain.model.QJobCategory.jobCategory;
import static com.mock.interview.category.domain.model.QJobPosition.jobPosition;
import static com.mock.interview.interviewquestion.domain.model.QInterviewQuestion.interviewQuestion;
import static com.mock.interview.user.domain.model.QUsers.*;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionRepositoryForListView {
    private final JPAQueryFactory query;
    private final int TOP_3 = 3;

    public List<ChildQuestionOverview> findChildQuestionTop3Likes(Long questionIdCond) {
        return query.select(
                        Projections.constructor(ChildQuestionOverview.class,
                                interviewQuestion.id, users.username, interviewQuestion.createdAt,
                                interviewQuestion.question, interviewQuestion.likes)
                )
                .from(interviewQuestion)
                .innerJoin(interviewQuestion.owner, users)
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
                        categoryIdEq(searchOptions.getCategoryIdCond()),
                        positionIdEq(searchOptions.getPositionIdCond()), ownerIdEq(searchOptions.getOwnerIdCond()),
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
                        categoryIdEq(searchOptions.getCategoryIdCond()),
                        positionIdEq(searchOptions.getPositionIdCond()),
                        ownerIdEq(searchOptions.getOwnerIdCond()),
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

    private BooleanExpression categoryIdEq(Long jobCategoryIdCond) {
        return jobCategoryIdCond == null ? null : interviewQuestion.category.id.eq(jobCategoryIdCond);
    }

    private BooleanExpression positionIdEq(Long jobPositionIdCond) {
        return jobPositionIdCond == null ? null : interviewQuestion.position.id.eq(jobPositionIdCond);
    }

    private BooleanExpression ownerIdEq(Long ownerIdCond) {
        return ownerIdCond == null ? null : interviewQuestion.owner.id.eq(ownerIdCond);
    }

    private BooleanExpression isVisible() {
        return interviewQuestion.isHidden.isFalse();
    }
}
