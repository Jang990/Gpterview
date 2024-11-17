package com.mock.interview.interview.domain;

import com.mock.interview.experience.domain.Experience;
import com.mock.interview.interview.domain.exception.RequiredExperienceTopicNotFoundException;
import com.mock.interview.interview.domain.exception.RequiredTechTopicNotFoundException;
import com.mock.interview.interview.presentation.dto.InterviewType;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InterviewTopicsDtoCreatorTest {
    private InterviewTopicsDtoCreator creator = new InterviewTopicsDtoCreator();

    @DisplayName("기술 주제가 필요한 면접 타입일 때, 기술 주제가 없다면 예외 발생")
    @Test
    void test1() {
        InterviewType type = mock(InterviewType.class);
        when(type.requiredTechTopics()).thenReturn(true);

        assertThrows(RequiredTechTopicNotFoundException.class,
                () -> creator.create(type, Collections.emptyList(), filledExperienceList())
        );
    }

    @DisplayName("경험 주제가 필요한 면접 타입일 때, 경험 주제가 없다면 예외 발생")
    @Test
    void test2() {
        InterviewType type = mock(InterviewType.class);
        when(type.requiredExperienceTopics()).thenReturn(true);

        assertThrows(RequiredExperienceTopicNotFoundException.class,
                () -> creator.create(type, filledTechList(), Collections.emptyList())
        );
    }


    @DisplayName("주제가 전부 필요없을 때, 주제를 빈 리스트로 초기화")
    @Test
    void test3() {
        InterviewType type = mock(InterviewType.class);
        when(type.requiredTechTopics()).thenReturn(false);
        when(type.requiredExperienceTopics()).thenReturn(false);

        InterviewTopicsDto result = creator.create(type, filledTechList(), filledExperienceList());

        assertEquals(type, result.getType());
        assertEquals(Collections.emptyList(), result.getTechTopics());
        assertEquals(Collections.emptyList(), result.getExperienceTopics());
    }

    @DisplayName("생성 성공 시 전달한 타입과 주제가 DTO에 담김")
    @Test
    void test4() {
        InterviewType type = mock(InterviewType.class);
        when(type.requiredTechTopics()).thenReturn(true);
        when(type.requiredExperienceTopics()).thenReturn(true);
        List<TechnicalSubjects> techs = filledTechList();
        List<Experience> experiences = filledExperienceList();

        InterviewTopicsDto result = creator.create(type, techs, experiences);

        assertEquals(type, result.getType());
        assertEquals(techs, result.getTechTopics());
        assertEquals(experiences, result.getExperienceTopics());
    }


    private List<TechnicalSubjects> filledTechList() {
        return List.of(mock(TechnicalSubjects.class));
    }

    private List<Experience> filledExperienceList() {
        return List.of(mock(Experience.class));
    }
}