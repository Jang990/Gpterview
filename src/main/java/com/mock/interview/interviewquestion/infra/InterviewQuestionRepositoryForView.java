package com.mock.interview.interviewquestion.infra;

import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.category.domain.model.QJobCategory;
import com.mock.interview.category.presentation.dto.JobCategoryView;
import com.mock.interview.interviewquestion.domain.exception.InterviewQuestionNotFoundException;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interviewquestion.presentation.dto.ChildQuestionOverview;
import com.mock.interview.interviewquestion.presentation.dto.QuestionOverview;
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
import static com.mock.interview.interviewquestion.domain.model.QInterviewQuestion.*;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InterviewQuestionRepositoryForView {
    private final JPAQueryFactory query;
    private final int TOP_3 = 3;

    public QuestionOverview findQuestion(Long loginIdCond, Long questionIdCond) {
        QJobCategory field = new QJobCategory("field");
        QJobCategory department = new QJobCategory("department");
        InterviewQuestion question = query.selectFrom(interviewQuestion)
                .leftJoin(interviewQuestion.appliedJob, field)
                .leftJoin(interviewQuestion.appliedJob.department, department)
                .where(interviewQuestion.id.eq(questionIdCond)) // TODO: 전체 공개 여부가 추가되면 여기도 로그인아이디에 따라 안보이도록 수정해줘야함.
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
                .where(interviewQuestion.parentQuestion.id.eq(questionIdCond))
                .orderBy(interviewQuestion.likes.desc(), interviewQuestion.createdAt.desc())
                .limit(TOP_3)
                .fetch();
    }

    public Page<QuestionOverview> findOverviewList(
            Long parentQuestionIdCond, String jobCategoryCond, String createdBy, Pageable pageable
    ) {
        QJobCategory field = new QJobCategory("field");
        QJobCategory department = new QJobCategory("department");
        List<InterviewQuestion> questions = query.selectFrom(interviewQuestion)
                .leftJoin(interviewQuestion.appliedJob, field)
                .leftJoin(interviewQuestion.appliedJob.department, department)
                .where(searchChildQuestion(parentQuestionIdCond), jobCategoryEq(jobCategoryCond), createdByEq(createdBy))
                .orderBy(interviewQuestion.likes.desc(), interviewQuestion.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<QuestionOverview> content = convert(questions);
        JPAQuery<Long> countQuery = query.select(interviewQuestion.count())
                .from(interviewQuestion)
                .leftJoin(interviewQuestion.appliedJob, jobCategory)
                .where(jobCategoryEq(jobCategoryCond), createdByEq(createdBy));

        // 현재 페이지의 요소 수가 Limit보다 적으면 Count쿼리를 날리지 않아서 PageImpl보다 좋음
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private List<QuestionOverview> convert(List<InterviewQuestion> questions) {
        return questions.stream().map(InterviewQuestionRepositoryForView::convert).toList();
    }

    private static QuestionOverview convert(InterviewQuestion question) {
        return new QuestionOverview(question.getId(), question.getCreatedBy(),
                convert(question.getAppliedJob()),
                question.getTechLink().stream().map(link -> link.getTechnicalSubjects().getName()).toList(),
                question.getQuestion(), question.getCreatedAt(), question.getLikes()
        );
    }

    private static JobCategoryView convert(JobCategory category) {
        if(category.isDepartment())
            return new JobCategoryView(category.getName(), null);

        return new JobCategoryView(category.getDepartment().getName(), category.getName());
    }

    private BooleanExpression searchChildQuestion(Long parentQuestionIdCond) {
        return parentQuestionIdCond == null ?
                null : interviewQuestion.parentQuestion.id.eq(parentQuestionIdCond);
    }

    private BooleanExpression jobCategoryEq(String jobCategoryCond) {
        return jobCategoryCond == null ? null : interviewQuestion.appliedJob.name.eq(jobCategoryCond)
                .or(interviewQuestion.appliedJob.department.name.eq(jobCategoryCond));
    }

    private BooleanExpression createdByEq(String createdBy) {
        return createdBy == null ? null : interviewQuestion.createdBy.eq(createdBy);
    }
}
