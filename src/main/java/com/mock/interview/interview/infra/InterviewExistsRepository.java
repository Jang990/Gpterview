package com.mock.interview.interview.infra;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.mock.interview.interview.domain.model.QInterview.*;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InterviewExistsRepository {
    private final JPAQueryFactory query;

    public boolean isExist(long interviewId, long userId) {
        Integer exist = query.selectOne()
                .from(interview)
                .where(interview.id.eq(interviewId), interview.users.id.eq(userId))
                .fetchFirst();

        return exist != null;
    }
}
