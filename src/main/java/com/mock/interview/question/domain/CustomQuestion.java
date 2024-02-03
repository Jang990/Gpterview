package com.mock.interview.question.domain;


import com.mock.interview.candidate.domain.model.CandidateConfig;
import com.mock.interview.candidate.presentation.dto.CandidateProfileForm;
import com.mock.interview.candidate.presentation.dto.InterviewConfigDto;
import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.global.auditing.BaseTimeEntity;
import com.mock.interview.tech.domain.model.CustomQuestionTechLink;
import com.mock.interview.tech.domain.model.ProfileTechLink;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import com.mock.interview.user.domain.model.Users;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomQuestion extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private long viewCount;

    @Column(nullable = false)
    private boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;

    @ManyToOne(fetch = FetchType.LAZY)
    private JobCategory field;

    @Cascade(CascadeType.ALL)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "question")
    private List<CustomQuestionTechLink> techLink = new ArrayList<>();

    public static CustomQuestion create(
            String content, Users users,
            JobCategory field, List<TechnicalSubjects> techList) {
        CustomQuestion question = new CustomQuestion();
        question.users = users;
        question.content = content;
        question.isDeleted = false;
        question.field = field;
        question.viewCount = 1;

        if(techList != null)
            question.techLink = createTechLinks(question, techList);

        return question;
    }

    private static List<CustomQuestionTechLink> createTechLinks(CustomQuestion question, List<TechnicalSubjects> techList) {
        return techList.stream().map((tech) -> CustomQuestionTechLink.createLink(question, tech)).toList();
    }
}
