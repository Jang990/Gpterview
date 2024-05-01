package com.mock.interview.interviewquestion.presentation.dto;

import com.mock.interview.category.presentation.dto.JobCategoryView;
import com.mock.interview.tech.presentation.dto.TechViewDto;
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
    private List<TechViewDto> techList; // Redis
    private String content; // Redis를 왜 사용하나요?
    private LocalDateTime createdAt;
    private Long parentQuestionId;
    private Long ownerId;
    private long like; // 32
    private boolean isHidden;
//    private long hits; // 조회수
//    private long commentCnt; 댓글 수
    // TODO: 조회수, 댓글수 미구현

}
