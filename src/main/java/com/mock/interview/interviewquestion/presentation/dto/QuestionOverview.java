package com.mock.interview.interviewquestion.presentation.dto;

import com.mock.interview.category.presentation.dto.JobCategoryView;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class QuestionOverview {
    private long id; // 1
    private String createdBy;
    private JobCategoryView category;
    private List<String> tech; // Redis
    private String content; // Redis를 왜 사용하나요?
    private LocalDateTime createdAt;
    private long hits; // 32
    private long commentCnt; // 3

    // 추천수 비추천수
}
