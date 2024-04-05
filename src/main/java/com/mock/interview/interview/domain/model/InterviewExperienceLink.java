package com.mock.interview.interview.domain.model;

import com.mock.interview.user.domain.model.Experience;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InterviewExperienceLink {
    @Id
    @Column(name = "experience_link_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_id")
    private Interview interview;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "experience_id")
    private Experience experience;

    static InterviewExperienceLink createLink(Interview interview, Experience experience) {
        InterviewExperienceLink link = new InterviewExperienceLink();
        link.interview = interview;
        link.experience = experience;
        return link;
    }
}
