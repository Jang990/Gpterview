package com.mock.interview.user.domain.model;

import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.category.domain.model.JobPosition;
import com.mock.interview.experience.domain.Experience;
import com.mock.interview.global.auditing.BaseTimeEntity;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    private String picture;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_category_id")
    private JobCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_position_id")
    private JobPosition position;

    @Cascade(CascadeType.ALL)
    @OneToMany(mappedBy = "users")
    private List<UsersTechLink> techLink = new ArrayList<>();

    @Cascade(CascadeType.ALL)
    @OneToMany(mappedBy = "users")
    private List<Experience> experiences = new ArrayList<>();


    public static Users createUser(String email, String username, String picture) {
        Users user = new Users();
        user.email = email;
        user.username = username;
        user.picture = picture;
        user.role = UserRole.USER;
        return user;
    }

    public void updatePicture(String picture) {
        this.picture = picture;
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

    public void linkPosition(JobPosition position) {
        if(category == null || position == null || category != position.getCategory())
            throw new IllegalArgumentException();
        this.position = position;
    }

    public void changeUsername(String username) {
        this.username = username;
    }

    public void removeCategory() {
        category = null;
        removePosition();
    }

    public void removePosition() {
        position = null;
    }

    public void replaceTech(List<TechnicalSubjects> techList) {
        techList = new LinkedList<>();
        linkTech(techList);
    }
}
