package com.mock.interview.user.domain.model;

import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.category.domain.model.JobPosition;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
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
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true)
    private String username;
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_category_id")
    private JobCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_position_id")
    private JobPosition position;

    @Cascade(CascadeType.ALL)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "users")
    private List<UsersTechLink> techLink = new ArrayList<>();

    @Cascade(CascadeType.ALL)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "users")
    private List<Experience> experiences = new ArrayList<>();


    public static Users createUser(String username, String password) {
        Users user = new Users();
        user.username = username;
        user.password = password;
        return user;
    }

    public void addExperience(String experience) {
        this.experiences.add(Experience.create(this, experience));
    }

    public void linkTech(List<TechnicalSubjects> techList) {
        if(techList == null || techList.isEmpty())
            throw new IllegalArgumentException();

        techList.forEach(this::linkTech);
    }

    public void linkTech(TechnicalSubjects tech) {
        if(tech == null)
            throw new IllegalArgumentException();

        techLink.add(UsersTechLink.createLink(this, tech));
    }

    public void linkCategory(JobCategory category) {
        if(category == null)
            throw new IllegalArgumentException();
        this.category = category;
    }

    public void linkJob(JobCategory category, JobPosition position) {
        linkCategory(category);
        if(position == null || !position.getCategory().getId().equals(category.getId()))
            throw new IllegalArgumentException("사용자에게 받은 category와 position간에 관계가 없습니다.");

        this.position = position;
    }
}
