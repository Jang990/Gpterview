package com.mock.interview.tech.infra;

import com.mock.interview.tech.domain.model.QTechnicalSubjects;
import com.mock.interview.tech.presentation.dto.TechnicalSubjectsResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.mock.interview.tech.domain.model.QTechnicalSubjects.*;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TechnicalSubjectsRepositoryForView {
    private final JPAQueryFactory query;
    public List<TechnicalSubjectsResponse> findContainTechs(String name, int size) {
        return query
                .select(
                        Projections.constructor(
                                TechnicalSubjectsResponse.class,
                                technicalSubjects.id, technicalSubjects.name
                        )
                )
                .from(technicalSubjects)
                .where(technicalSubjects.name.contains(name))
                .orderBy(technicalSubjects.name.asc())
                .limit(size)
                .fetch();
    }
}
