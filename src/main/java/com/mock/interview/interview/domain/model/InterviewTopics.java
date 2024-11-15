package com.mock.interview.interview.domain.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.ArrayList;
import java.util.List;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InterviewTopics {
    @Cascade(CascadeType.ALL)
    @OneToMany(mappedBy = "interview")
    private List<InterviewTechLink> techLink = new ArrayList<>();

    @Cascade(CascadeType.ALL)
    @OneToMany(mappedBy = "interview")
    private List<InterviewExperienceLink> experienceLink = new ArrayList<>();
}
