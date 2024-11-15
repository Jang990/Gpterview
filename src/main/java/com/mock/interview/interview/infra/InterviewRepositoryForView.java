package com.mock.interview.interview.infra;

import com.mock.interview.interview.application.helper.InterviewConvertor;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.presentation.dto.InterviewOverview;
import com.mock.interview.interview.presentation.dto.InterviewOverviewFragment;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.mock.interview.interview.domain.model.QInterview.*;

@Repository
@RequiredArgsConstructor
public class InterviewRepositoryForView {
    private final JPAQueryFactory query;

    public List<InterviewOverviewFragment> findInterviewOverview(long userId, Pageable pageable) {
        return query.select(
                    Projections.constructor(InterviewOverviewFragment.class,
                            interview.id,
                            interview.title.title,
                            interview.timer.durationMinutes,
                            interview.timer.startedAt
                    )
                )
                .from(interview)
                .where(interview.candidateInfo.users.id.eq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(interview.timer.startedAt.desc())
                .fetch();
    }

    public Page<InterviewOverview> findInterviewList(long userId, Pageable pageable) {
        List<Interview> result = query.selectFrom(interview)
                .where(interview.candidateInfo.users.id.eq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(interview.timer.startedAt.desc())
                .fetch();

        List<InterviewOverview> content = InterviewConvertor.convert(result);
        JPAQuery<Long> countQuery = query.select(interview.count()).from(interview)
                .where(interview.candidateInfo.users.id.eq(userId));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}
