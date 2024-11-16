package com.mock.interview.interview.infra;

import com.mock.interview.ExternalDBTest;
import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.category.domain.model.JobPosition;
import com.mock.interview.interview.domain.ActiveInterviewFinder;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.domain.model.TestInterviewBuilder;
import com.mock.interview.user.domain.model.Users;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

import static com.mock.interview.interview.TimeUtils.*;
import static org.junit.jupiter.api.Assertions.*;

@ExternalDBTest
class ActiveInterviewFinderImplTest {

    @Autowired
    EntityManager em;

    @Autowired
    JPAQueryFactory query;

    ActiveInterviewFinder finder;
    private Users users;

    private TestInterviewBuilder builderForPersist() {
        JobCategory category = JobCategory.createCategory("AAA");
        JobPosition position = JobPosition.create("BBB", category);
        em.persist(category);
        em.persist(position);
        return TestInterviewBuilder.builder()
                .category(category)
                .position(position)
                .techTopics(Collections.EMPTY_LIST)
                .experienceTopics(Collections.EMPTY_LIST);
    }

    @BeforeEach
    void beforeEach() {
        finder = new ActiveInterviewFinderImpl(query);

        users = Users.createUser("ABC@gmail.com", "ABC", null);
        em.persist(users);
    }

    @DisplayName("기준 시간에 활성화된 면접이 있다면 true를 반환한다.")
    @Test
    void test1() {
        Interview interview = builderForPersist()
                .timer(time(0, 0), time(0, 30))
                .users(users)
                .build();
        em.persist(interview);
        em.flush();
        em.clear();

        assertTrue(finder.hasActiveInterview(users, time(0, 20)));
    }

    @DisplayName("기준 시간에 활성화된 면접이 있다면 false를 반환한다.")
    @Test
    void test2() {
        Interview interview = builderForPersist()
                .timer(time(0, 0), time(0, 30))
                .users(users)
                .build();
        em.persist(interview);
        em.flush();
        em.clear();

        assertFalse(finder.hasActiveInterview(users, time(0, 40)));
    }

    @DisplayName("시작시간 <= 기준 < 만료시간의 범위를 갖는다.")
    @Test
    void test3() {
        Interview interview = builderForPersist()
                .timer(time(0, 0), time(0, 30))
                .users(users)
                .build();
        em.persist(interview);
        em.flush();
        em.clear();

        assertTrue(finder.hasActiveInterview(users, time(0, 0)));
        assertFalse(finder.hasActiveInterview(users, time(0, 30)));
    }

}