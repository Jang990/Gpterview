package com.mock.interview.interview.domain.model;

import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.category.domain.model.JobPosition;
import com.mock.interview.user.domain.model.Users;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CandidateInfoCreatorTest {
    private CandidateInfoCreator creator = new CandidateInfoCreator();

    @DisplayName("지원자 정보중 하나라도 null이 있다면 예외가 발생합니다.")
    @Test
    void test1() {
        assertThrows(IllegalArgumentException.class,
                () -> creator.create(mock(Users.class), null, null));

        assertThrows(IllegalArgumentException.class,
                () -> creator.create(mock(Users.class), mock(JobCategory.class), null));

        assertThrows(IllegalArgumentException.class,
                () -> creator.create(null, null, null));
    }

    @DisplayName("카테고리와 포지션간에 연관관계가 없다면 예외가 발생합니다.")
    @Test
    void test2() {
        JobCategory category = mock(JobCategory.class);
        JobPosition position = mock(JobPosition.class);
        when(position.isRelatedTo(any())).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> creator.create(mock(Users.class), category, position));
    }

    @DisplayName("정상 처리가 되면 CandidateInfo를 생성합니다.")
    @Test
    void test3() {
        Users users = mock(Users.class);
        JobCategory category = mock(JobCategory.class);
        JobPosition position = mock(JobPosition.class);
        when(position.isRelatedTo(any())).thenReturn(true);

        CandidateInfo result = creator.create(users, category, position);

        assertEquals(users, result.getUsers());
        assertEquals(category, result.getCategory());
        assertEquals(position, result.getPosition());
    }

}