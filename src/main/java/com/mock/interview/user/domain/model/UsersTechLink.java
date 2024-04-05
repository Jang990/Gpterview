package com.mock.interview.user.domain.model;

import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UsersTechLink {
    @Id
    @Column(name = "tech_link_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "technical_subjects_id")
    private TechnicalSubjects technicalSubjects;

    static UsersTechLink createLink(Users users, TechnicalSubjects tech) {
        UsersTechLink link = new UsersTechLink();
        link.users = users;
        link.technicalSubjects = tech;
        return link;
    }
}
