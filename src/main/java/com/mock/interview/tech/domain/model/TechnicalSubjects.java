package com.mock.interview.tech.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 기술과 관련 이름 저장
 * Spring, Prometheus, MySQL, Jenkins, Python 등등
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TechnicalSubjects {
    @Id
    @Column(name = "technical_subjects_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    public static TechnicalSubjects create(String name) {
        TechnicalSubjects subjects = new TechnicalSubjects();
        insertName(name, subjects);
        return subjects;
    }

    private static void insertName(String name, TechnicalSubjects subjects) {
        subjects.name = name.toUpperCase();
    }
}
