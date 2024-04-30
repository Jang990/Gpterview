package com.mock.interview.experience.infra;

import com.mock.interview.experience.domain.QExperience;
import com.mock.interview.experience.presentation.dto.ExperienceDto;
import com.mock.interview.experience.presentation.dto.ExperienceView;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.mock.interview.experience.domain.QExperience.*;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ExperienceRepositoryView {
    private final JPAQueryFactory query;

    public List<ExperienceDto> findExperienceList(long userId) {
        return query.select(
                    Projections.constructor(ExperienceDto.class, experience.id, experience.content)
                )
                .from(experience)
                .where(experience.users.id.eq(userId))
                .fetch();
    }
}
