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
    private double cosineSimilarity;

    public QuestionMetaData(long id, Long parentQuestionId, String field, List<String> tech, List<String> necessaryWords, long likes) {
        this.id = id;
        this.parentQuestionId = parentQuestionId;
        this.field = field;
        this.tech = tech;
        this.necessaryWords = necessaryWords;
        this.likes = likes;
    }

    public void setCosineSimilarity(double cosineSimilarity) {
        this.cosineSimilarity = cosineSimilarity;
    }
}
