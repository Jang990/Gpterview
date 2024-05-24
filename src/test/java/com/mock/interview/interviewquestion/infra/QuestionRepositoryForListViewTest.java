package com.mock.interview.interviewquestion.infra;

import com.mock.interview.interviewquestion.presentation.dto.QuestionSearchCond;
import com.mock.interview.interviewquestion.presentation.dto.QuestionSearchOptionsDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
class QuestionRepositoryForListViewTest {
    @Autowired
    QuestionRepositoryForListView questionRepositoryForListView;

//    @Test
    void test1() {
        QuestionSearchOptionsDto searchOptions =
                QuestionSearchOptionsDto.builder()
                        .techIdCond(6L)
                        .build();
        questionRepositoryForListView.findOverviewList(searchOptions, PageRequest.of(3, 10));
    }

}