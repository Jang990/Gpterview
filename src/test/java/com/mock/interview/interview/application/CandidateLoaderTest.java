package com.mock.interview.interview.application;

import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.category.domain.model.JobPosition;
import com.mock.interview.category.infra.JobCategoryRepository;
import com.mock.interview.category.infra.JobPositionRepository;
import com.mock.interview.interview.domain.model.CandidateInfo;
import com.mock.interview.interview.domain.model.CandidateInfoCreator;
import com.mock.interview.user.domain.model.Users;
import com.mock.interview.user.infrastructure.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CandidateLoaderTest {
    private final UserRepository userRepository = mock(UserRepository.class);
    private final JobCategoryRepository categoryRepository = mock(JobCategoryRepository.class);
    private final JobPositionRepository positionRepository = mock(JobPositionRepository.class);
    private CandidateLoader candidateLoader = new CandidateLoader(
            userRepository,
            categoryRepository,
            positionRepository,
            new CandidateInfoCreator()
    );

    @DisplayName("Repository에서 불러온 정보를 바탕으로 생성한 지원자 정보를 반환한다.")
    @Test
    void test1() {
        Users users = mock(Users.class);
        JobCategory category = mock(JobCategory.class);
        JobPosition position = mock(JobPosition.class);
        when(position.isRelatedTo(category)).thenReturn(true);
        when(userRepository.findForInterviewSetting(anyLong())).thenReturn(Optional.of(users));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(positionRepository.findById(anyLong())).thenReturn(Optional.of(position));

        CandidateInfo result = candidateLoader.load(1L, 1L, 1L);

        assertEquals(users, result.getUsers());
        assertEquals(category, result.getCategory());
        assertEquals(position, result.getPosition());
    }

}