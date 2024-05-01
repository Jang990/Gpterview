package com.mock.interview.questiontoken.domain;

import com.mock.interview.global.StringListConverter;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionTokenization {
    @Id
    @Column(name = "question_token_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = StringListConverter.class)
    @Column(name = "tokens", nullable = false, length = 700)
    private List<String> result;

    public static QuestionTokenization create(String question, KoreaStringAnalyzer analyzer) {
        QuestionTokenization questionTokenization = new QuestionTokenization();
        questionTokenization.result = analyzer.extractNecessaryTokens(question);
        return questionTokenization;
    }
}
