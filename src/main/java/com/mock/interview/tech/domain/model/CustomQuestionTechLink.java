package com.mock.interview.tech.domain.model;

import com.mock.interview.question.domain.CustomQuestion;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomQuestionTechLink {
    @Id
    @Column(name = "question_tech_link_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "custom_question_id")
    private CustomQuestion question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "technical_subjects_id")
    private TechnicalSubjects technicalSubjects;

    public static CustomQuestionTechLink createLink(CustomQuestion question, TechnicalSubjects tech) {
        CustomQuestionTechLink link = new CustomQuestionTechLink();
        link.question = question;
        link.technicalSubjects = tech;
        return link;
    }
}
