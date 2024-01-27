package com.mock.interview.interview.infrastructure;

import com.mock.interview.candidate.domain.model.QCandidateConfig;
import com.mock.interview.conversation.presentation.dto.MessageDto;
import com.mock.interview.interview.domain.model.QInterview;
import com.mock.interview.interview.presentation.dto.InterviewOverviewFragment;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.mock.interview.candidate.domain.model.QCandidateConfig.*;
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
                            interview.candidateConfig.durationMinutes,
                            interview.createdAt
                    )
                )
                .from(interview)
                .join(interview.candidateConfig, candidateConfig)
                .where(interview.users.id.eq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(interview.createdAt.desc())
                .fetch();
    }
}
