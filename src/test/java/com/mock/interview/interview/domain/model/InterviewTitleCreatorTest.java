package com.mock.interview.interview.domain.model;

import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.category.domain.model.JobPosition;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class InterviewTitleCreatorTest {

    private InterviewTitleCreator titleCreator = new InterviewTitleCreator();

    @DisplayName("기본 면접 제목은 카테고리와 포지션 이름의 조합이다.")
    @Test
    void test1() {
        JobCategory category = JobCategory.createCategory("IT");
        JobPosition position = JobPosition.create("백엔드", category);

        InterviewTitle defaultTitle = titleCreator.createDefault(category, position);

        assertThat(defaultTitle.getTitle()).contains("IT", "백엔드");
    }

}