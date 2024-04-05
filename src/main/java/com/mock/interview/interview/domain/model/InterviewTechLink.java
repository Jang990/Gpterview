package com.mock.interview.interview.domain.model;

import com.mock.interview.tech.domain.model.TechnicalSubjects;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InterviewTechLink {
    @Id
    @Column(name = "tech_link_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_id")
    private Interview interview;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "technical_subjects_id")
    private TechnicalSubjects technicalSubjects;

    static InterviewTechLink createLink(Interview interview, TechnicalSubjects tech) {
        InterviewTechLink link = new InterviewTechLink();
        link.interview = interview;
        link.technicalSubjects = tech;
        return link;
    }
}
