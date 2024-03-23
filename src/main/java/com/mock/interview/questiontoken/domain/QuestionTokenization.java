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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id",nullable = false)
    private InterviewQuestion interviewQuestion;

    @Convert(converter = StringListConverter.class)
    @Column(name = "tokens", nullable = false)
    private List<String> result;

    public static QuestionTokenization create(InterviewQuestion interviewQuestion, KoreaStringAnalyzer analyzer) {
        QuestionTokenization questionTokenization = new QuestionTokenization();
        questionTokenization.interviewQuestion = interviewQuestion;
        questionTokenization.result = analyzer.extractNecessaryTokens(interviewQuestion.getQuestion());
        return questionTokenization;
    }
}
