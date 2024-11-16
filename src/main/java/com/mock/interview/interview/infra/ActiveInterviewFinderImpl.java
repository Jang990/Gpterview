package com.mock.interview.interview.infra;

import com.mock.interview.interview.domain.ActiveInterviewFinder;
import com.mock.interview.user.domain.model.Users;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.mock.interview.interview.domain.model.QInterview.interview;

@Service
@RequiredArgsConstructor
public class ActiveInterviewFinderImpl implements ActiveInterviewFinder {

    private final JPAQueryFactory query;

    @Override
    public boolean hasActiveInterview(Users users, LocalDateTime now) {
        Integer exist = query.selectOne()
                .from(interview)
                .where(interview.candidateInfo.users.id.eq(users.getId()),
                        interview.timer.startedAt.eq(now).or(interview.timer.startedAt.before(now)),
                        interview.timer.expiredAt.after(now)
                )
                .fetchFirst();

        return exist != null;
    }
}
