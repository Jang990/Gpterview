package com.mock.interview.interviewquestion.infra;

import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.category.domain.model.QJobCategory;
import com.mock.interview.category.presentation.dto.JobCategoryView;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
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

import static com.mock.interview.category.domain.model.QJobCategory.*;
import static com.mock.interview.interviewquestion.domain.model.QInterviewQuestion.*;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InterviewQuestionRepositoryForView {
    private final JPAQueryFactory query;

    public Page<QuestionOverview> findOverviewListWithJobCategory(
            String jobCategoryCond, String createdBy, Pageable pageable
    ) {
        QJobCategory field = new QJobCategory("field");
        QJobCategory department = new QJobCategory("department");
        List<InterviewQuestion> questions = query.selectFrom(interviewQuestion)
                .leftJoin(interviewQuestion.appliedJob, field)
                .leftJoin(interviewQuestion.appliedJob.department, department)
                .where(jobCategoryEq(jobCategoryCond), createdByEq(createdBy))
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
        return questions.stream().map(q ->
                new QuestionOverview(q.getId(), q.getCreatedBy(),
                        convert(q.getAppliedJob()),
                        q.getTechLink().stream().map(link -> link.getTechnicalSubjects().getName()).toList(),
                        q.getQuestion(), q.getCreatedAt(), 123, 321
                )
        ).toList();
    }

    private static JobCategoryView convert(JobCategory category) {
        if(category.isDepartment())
            return new JobCategoryView(category.getName(), null);

        return new JobCategoryView(category.getDepartment().getName(), category.getName());
    }

    private BooleanExpression jobCategoryEq(String jobCategoryCond) {
        return jobCategoryCond == null ? null : interviewQuestion.appliedJob.name.eq(jobCategoryCond)
                .or(interviewQuestion.appliedJob.department.name.eq(jobCategoryCond));
    }

    private BooleanExpression createdByEq(String createdBy) {
        return createdBy == null ? null : interviewQuestion.createdBy.eq(createdBy);
    }
}
