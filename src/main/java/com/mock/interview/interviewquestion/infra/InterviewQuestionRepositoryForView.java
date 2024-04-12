package com.mock.interview.interviewquestion.infra;

import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.category.domain.model.JobPosition;
import com.mock.interview.category.presentation.dto.JobCategoryView;
import com.mock.interview.interviewquestion.application.QuestionConvertor;
import com.mock.interview.interviewquestion.domain.exception.InterviewQuestionNotFoundException;
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

import static com.mock.interview.category.domain.model.QJobCategory.*;
import static com.mock.interview.category.domain.model.QJobPosition.*;
import static com.mock.interview.interviewquestion.domain.model.QInterviewQuestion.*;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InterviewQuestionRepositoryForView {
    private final JPAQueryFactory query;
    private final int TOP_3 = 3;

    public QuestionOverview findQuestion(Long loginIdCond, Long questionIdCond) {
        InterviewQuestion question = query.selectFrom(interviewQuestion)
                .leftJoin(interviewQuestion.category, jobCategory)
                .leftJoin(interviewQuestion.position, jobPosition)
                .where(questionIdEq(questionIdCond), ownerIdEq(loginIdCond), isActive()) // TODO: 전체 공개 여부가 추가되면 여기도 로그인아이디에 따라 안보이도록 수정해줘야함.
                .fetchOne();

        if(question == null)
            throw new InterviewQuestionNotFoundException();

        return convert(question);
    }

    public List<ChildQuestionOverview> findChildQuestionTop3Likes(Long questionIdCond) {
        return query.select(
                        Projections.constructor(ChildQuestionOverview.class,
                                interviewQuestion.id, interviewQuestion.createdBy, interviewQuestion.createdAt,
                                interviewQuestion.question, interviewQuestion.likes)
                )
                .from(interviewQuestion)
                .where(findChildQuestion(questionIdCond), isActive())
                .orderBy(interviewQuestion.likes.desc(), interviewQuestion.createdAt.desc())
                .limit(TOP_3)
                .fetch();
    }

    // TODO: tech 검색 미지원
    public Page<QuestionOverview> findOverviewList(
            QuestionSearchOptionsDto searchOptions,
            Pageable pageable
    ) {
        if(searchOptions == null)
            searchOptions = new QuestionSearchOptionsDto();
        if(searchOptions.getSearchCond() == null)
            searchOptions.setSearchCond(new QuestionSearchCond());

        List<InterviewQuestion> questions = query.selectFrom(interviewQuestion)
                .leftJoin(interviewQuestion.category, jobCategory)
                .leftJoin(interviewQuestion.position, jobPosition)
                .where(
                        findChildQuestion(searchOptions.getParentQuestionIdCond()),
                        categoryEq(searchOptions.getCategoryNameCond()),
                        positionEq(searchOptions.getPositionNameCond()), createdByEq(searchOptions.getCreatedByCond()),
                        questionTypeEq(searchOptions.getSearchCond().getTypeCond()),
                        keywordContains(searchOptions.getSearchCond().getKeywordCond()),
                        isActive()
                )
                .orderBy(interviewQuestion.likes.desc(), interviewQuestion.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<QuestionOverview> content = convert(questions);
        JPAQuery<Long> countQuery = query.select(interviewQuestion.count())
                .from(interviewQuestion)
                .leftJoin(interviewQuestion.category, jobCategory)
                .leftJoin(interviewQuestion.position, jobPosition)
                .where(
                        findChildQuestion(searchOptions.getParentQuestionIdCond()),
                        categoryEq(searchOptions.getCategoryNameCond()),
                        positionEq(searchOptions.getPositionNameCond()),
                        createdByEq(searchOptions.getCreatedByCond()),
                        questionTypeEq(searchOptions.getSearchCond().getTypeCond()),
                        keywordContains(searchOptions.getSearchCond().getKeywordCond()),
                        isActive()
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

    private List<QuestionOverview> convert(List<InterviewQuestion> questions) {
        return questions.stream().map(InterviewQuestionRepositoryForView::convert).toList();
    }

    private static QuestionOverview convert(InterviewQuestion question) {
        return new QuestionOverview(question.getId(), question.getCreatedBy(),
                convert(question.getCategory(), question.getPosition()),
                question.getTechLink().stream().map(link -> link.getTechnicalSubjects().getName()).toList(),
                question.getQuestion(), question.getCreatedAt(), question.getLikes()
        );
    }

    private static JobCategoryView convert(JobCategory category, JobPosition position) {
        return new JobCategoryView(
                category == null ? null : category.getName(),
                position == null ? null : position.getName()
        );
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

    private BooleanExpression ownerIdEq(Long loginIdCond) {
        return loginIdCond == null ? null : interviewQuestion.owner.id.eq(loginIdCond);
    }

    private BooleanExpression isActive() {
        return interviewQuestion.isDeleted.isFalse();
    }

    private BooleanExpression questionIdEq(Long questionIdCond) {
        return questionIdCond == null ? null : interviewQuestion.id.eq(questionIdCond);
    }
}
