package com.mock.interview.tech.domain.model;

import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionTechLink {
    @Id
    @Column(name = "tech_link_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_question_id")
    private InterviewQuestion question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "technical_subjects_id")
    private TechnicalSubjects technicalSubjects;

    public static QuestionTechLink createLink(InterviewQuestion question, TechnicalSubjects tech) {
        QuestionTechLink link = new QuestionTechLink();
        link.question = question;
        link.technicalSubjects = tech;
        return link;
    }
}
