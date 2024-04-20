package com.mock.interview.experience.infra;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.mock.interview.experience.domain.QExperience.*;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ExperienceExistsRepository {
    private final JPAQueryFactory query;

    public Integer exist(long experienceId, long userId) {
        return query.selectOne()
                .from(experience)
                .where(experience.id.eq(experienceId), experience.users.id.eq(userId))
                .fetchFirst();
    }
}
