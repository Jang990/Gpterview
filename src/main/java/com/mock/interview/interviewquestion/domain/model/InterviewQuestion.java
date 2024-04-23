package com.mock.interview.interviewquestion.domain.model;

import com.mock.interview.experience.domain.Experience;
import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.category.domain.model.JobPosition;
import com.mock.interview.global.Events;
import com.mock.interview.interviewquestion.domain.event.QuestionCreatedEvent;
import com.mock.interview.interviewquestion.infra.InterviewQuestionRepository;
import com.mock.interview.global.auditing.BaseEntity;
import com.mock.interview.interviewanswer.domain.model.InterviewAnswer;
import com.mock.interview.questiontoken.domain.QuestionTokenization;
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
public class InterviewQuestion extends BaseEntity {
    @Id
    @Column(name = "interview_question_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "preferred_answer_id")
    private InterviewAnswer preferredAnswer;

    /** GPT가 질문을 생성할 수도 있기 떄문에 Owner와 createdBy는 다를 수 있음. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Users owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_category_id")
    private JobCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_position_id")
    private JobPosition position;

    @Column(nullable = false, length = 600)
    private String question;

    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    private long likes;

    @Cascade(CascadeType.ALL)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "question")
    private List<QuestionTechLink> techLink = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_question_id")
    private InterviewQuestion parentQuestion;

    @Cascade(CascadeType.ALL)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "interviewQuestion")
    private QuestionTokenization questionToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "related_experience_id")
    private Experience experience;

    public static InterviewQuestion create(
            InterviewQuestionRepository repository, String content, Users users,
            QuestionType questionType, String createdBy
    ) {
        InterviewQuestion question = new InterviewQuestion();
        question.question = content;
        question.owner = users;
        question.questionType = questionType;
        question.createdBy = createdBy;
        question.likes = 0;

        repository.save(question);
        Events.raise(new QuestionCreatedEvent(question.getId()));
        return question;
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

    public void linkTech(List<TechnicalSubjects> techList) {
        if(techList == null || techList.isEmpty())
            return;

        techList.forEach(this::linkTech);
    }

    public void linkTech(TechnicalSubjects tech) {
        if(tech == null)
            return;

        techLink.add(QuestionTechLink.createLink(this, tech));
    }

    public void linkExperience(Experience experience) {
        if (this.questionType != QuestionType.EXPERIENCE)
            throw new IllegalStateException();
        this.experience = experience;
    }

    public void like() {
        likes++;
    }

    public void cancelLike() {
        if(likes <= 0)
            throw new IllegalStateException();
        likes--;
    }

    // TODO: boolean 공개여부 추가
    // TODO: 조회 수 추가하기.
    // TODO: 질문에 대한 댓글 추가
}
