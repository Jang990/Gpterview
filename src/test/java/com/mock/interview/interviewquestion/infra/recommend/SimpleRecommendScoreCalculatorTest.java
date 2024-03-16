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
        List<List<String>> questionContents = simpleTestData.stream()
                .map(QuestionMetaData::getNecessaryWords).toList();
        CurrentQuestion user = new CurrentQuestion(1l, Arrays.asList("Spring", "MVC", "대해", "아는대로", "설명", "해보세요"), "Spring Boot", "백엔드"); // Spring MVC에 대해 아는대로 설명해보세요.
        System.out.println("TF-IDF\t좋아요\t필드 \t테크 \t꼬리질문");
        for (QuestionMetaData data : simpleTestData) {
            calculator.calculateScore(user, data);
            System.out.println(data.getNecessaryWords());
        }
    }

    static List<QuestionMetaData> createSimpleTestData() {
        return Arrays.asList(
                new QuestionMetaData(2, 1L, "프론트엔드", Arrays.asList("React"), Arrays.asList("React", "의", "장점", "무엇"), 45), // React의 장점은 무엇인가요?
                new QuestionMetaData(3, null, "백엔드", Arrays.asList("데이터베이스", "MySQL"), Arrays.asList("MySQL", "인덱스", "역할", "무엇"), 18), // MySQL에서 인덱스의 역할은 무엇인가요?
                new QuestionMetaData(4, 2L, "백엔드", Arrays.asList("Spring Boot"), Arrays.asList("Spring", "Boot", "의존", "성", "주입", "방법"), 55), // Spring Boot에서의 의존성 주입 방법은?
                new QuestionMetaData(5, null, "백엔드", Arrays.asList("Spring Boot", "Spring Security","OAuth"), Arrays.asList("OAuth", "의", "작동", "원리", "무엇"), 70), // OAuth의 작동 원리는 무엇인가요?
                new QuestionMetaData(6, null, "DBA", Arrays.asList("PostgreSQL"), Arrays.asList("PostgreSQL", "의", "트랜잭션", "처리", "방법"), 27), // PostgreSQL의 트랜잭션 처리 방법은?
                new QuestionMetaData(7, 3L, "백엔드", Arrays.asList("데이터베이스", "MongoDB"), Arrays.asList("MongoDB", "의", "장점", "무엇"), 42), // MongoDB의 장점은 무엇인가요?
                new QuestionMetaData(8, null, "프론트엔드", Arrays.asList("Vue.js"), Arrays.asList("Vue", "js", "양", "방향", "데이터", "바인딩"), 63), // Vue.js에서의 양방향 데이터 바인딩이란?
                new QuestionMetaData(9, 4L, "백엔드", Arrays.asList("Java"), Arrays.asList("Django", "ORM", "사용", "방법"), 51), // Java에서의 멀티스레딩 방법은?
                new QuestionMetaData(10, 4L, "보안", Arrays.asList("JWT"), Arrays.asList("JWT", "의", "구조", "어떻게", "되나요"), 38), // JWT의 구조는 어떻게 되나요?
                new QuestionMetaData(12, 6L, "백엔드", Arrays.asList("SQL"), Arrays.asList("SQL", "의", "JOIN", "구문", "종류", "무엇", "있나요"), 47), // SQL의 JOIN 구문 종류는 무엇이 있나요?
                new QuestionMetaData(13, 4L, "프론트엔드", Arrays.asList(), Arrays.asList("Spring", "DI", "와", "IOC", "대해", "설명", "해보세요"), 56), // Spring에서 DI와 IOC에 대해서 설명해보세요.
                new QuestionMetaData(14, null, "보안", Arrays.asList("SSL"), Arrays.asList("SSL", "의", "동작", "원리", "무엇"), 39), // SSL의 동작 원리는 무엇인가요?
                new QuestionMetaData(15, 7L, "백엔드", Arrays.asList("Flask"), Arrays.asList("Flask", "블루", "프린트", "란", "무엇"), 24), // Flask에서의 블루프린트란 무엇인가요?
                new QuestionMetaData(16, null, "데이터베이스", Arrays.asList("NoSQL"), Arrays.asList("NoSQL", "의", "특징", "무엇"), 33), // NoSQL의 특징은 무엇인가요?
                new QuestionMetaData(17, 8L, "프론트엔드", Arrays.asList("JavaScript","jQuery"), Arrays.asList("jQuery", "의", "사용", "목적", "무엇"), 44), // jQuery의 사용 목적은 무엇인가요?
                new QuestionMetaData(18, 9L, "보안", Arrays.asList("AES"), Arrays.asList("AES", "의", "암호", "화", "과정", "어떻게", "이루어지나요"), 57), // AES의 암호화 과정은 어떻게 이루어지나요?
                new QuestionMetaData(19, 10L, "모바일", Arrays.asList("SQLite"), Arrays.asList("SQLite", "의", "특징", "무엇"), 36), // SQLite의 특징은 무엇인가요?
                new QuestionMetaData(20, 11L, "보안", Arrays.asList("Firewall"), Arrays.asList("Firewall", "의", "역할", "무엇"), 49) // Firewall의 역할은 무엇인가요?;
        );
    }
}