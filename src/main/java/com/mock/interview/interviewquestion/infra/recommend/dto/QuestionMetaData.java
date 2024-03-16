package com.mock.interview.interviewquestion.infra.recommend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
public class QuestionMetaData {
    private long id; // 1
    private Long parentQuestionId; // 상위 질문 ID
    private String field; // 백엔드
    private List<String> tech; // [Spring Boot]
    private List<String> necessaryWords; // [Spring, Boot, 의존, 성, 주입, 방법]
    private long likes; // 32
}
