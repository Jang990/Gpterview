package com.mock.interview.interviewquestion.infra.recommend;

import com.mock.interview.interviewquestion.infra.recommend.dto.QuestionMetaData;
import com.mock.interview.interviewquestion.infra.recommend.dto.CurrentQuestion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class SimpleRecommendScoreCalculatorTest {
    SimpleRecommendScoreCalculator calculator;

    @BeforeEach
    void beforeEach() {
        calculator = new SimpleRecommendScoreCalculator();
    }

    @Test
    void test() {
        List<QuestionMetaData> simpleTestData = createSimpleTestData();
        CurrentQuestion user = new CurrentQuestion(1l, "Spring MVC에 관해서 아는대로 설명해보세요.", "Spring Boot", "백엔드");
        System.out.println("TF-DIF\t좋아요\t필드 \t테크 \t꼬리질문");
        for (QuestionMetaData data : simpleTestData) {
            calculator.calculateScore(user, data);
        }
    }

    List<QuestionMetaData> createSimpleTestData() {
        return Arrays.asList(
            new QuestionMetaData(2, 1L, "프론트엔드", Arrays.asList("React"), "React의 장점은 무엇인가요?", 45),
            new QuestionMetaData(3, null, "백엔드", Arrays.asList("데이터베이스", "MySQL"), "MySQL에서 인덱스의 역할은 무엇인가요?", 18),
            new QuestionMetaData(4, 2L, "백엔드", Arrays.asList("Spring Boot"), "Spring Boot에서의 의존성 주입 방법은?", 55),
            new QuestionMetaData(5, null, "백엔드", Arrays.asList("Spring Boot", "Spring Security","OAuth"), "OAuth의 작동 원리는 무엇인가요?", 70),
            new QuestionMetaData(6, null, "DBA", Arrays.asList("PostgreSQL"), "PostgreSQL의 트랜잭션 처리 방법은?", 27),
            new QuestionMetaData(7, 3L, "백엔드", Arrays.asList("데이터베이스", "MongoDB"), "MongoDB의 장점은 무엇인가요?", 42),
            new QuestionMetaData(8, null, "프론트엔드", Arrays.asList("Vue.js"), "Vue.js에서의 양방향 데이터 바인딩이란?", 63),
            new QuestionMetaData(9, 4L, "백엔드", Arrays.asList("Java"), "Java에서의 멀티스레딩 방법은?", 51),
            new QuestionMetaData(10, 4L, "보안", Arrays.asList("JWT"), "JWT의 구조는 어떻게 되나요?", 38),
            new QuestionMetaData(11, null, "백엔드", Arrays.asList("Django"), "Django에서의 ORM 사용 방법은?", 29),
            new QuestionMetaData(12, 6L, "백엔드", Arrays.asList("SQL"), "SQL의 JOIN 구문 종류는 무엇이 있나요?", 47),
            new QuestionMetaData(13, 4L, "프론트엔드", Arrays.asList(), "Spring에서 DI와 IOC에 대해서 설명해보세요.", 56),
            new QuestionMetaData(14, null, "보안", Arrays.asList("SSL"), "SSL의 동작 원리는 무엇인가요?", 39),
            new QuestionMetaData(15, 7L, "백엔드", Arrays.asList("Flask"), "Flask에서의 블루프린트란 무엇인가요?", 24),
            new QuestionMetaData(16, null, "데이터베이스", Arrays.asList("NoSQL"), "NoSQL의 특징은 무엇인가요?", 33),
            new QuestionMetaData(17, 8L, "프론트엔드", Arrays.asList("JavaScript","jQuery"), "jQuery의 사용 목적은 무엇인가요?", 44),
            new QuestionMetaData(18, 9L, "보안", Arrays.asList("AES"), "AES의 암호화 과정은 어떻게 이루어지나요?", 57),
            new QuestionMetaData(19, 10L, "모바일", Arrays.asList("SQLite"), "SQLite의 특징은 무엇인가요?", 36),
            new QuestionMetaData(20, 11L, "보안", Arrays.asList("Firewall"), "Firewall의 역할은 무엇인가요?", 49)
        );
    }
}