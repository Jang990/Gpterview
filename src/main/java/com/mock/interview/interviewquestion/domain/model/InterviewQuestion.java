package com.mock.interview.interviewquestion.domain.model;

import com.mock.interview.experience.domain.Experience;
import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.category.domain.model.JobPosition;
import com.mock.interview.global.Events;
import com.mock.interview.interviewquestion.domain.event.QuestionCreatedEvent;
import com.mock.interview.interviewquestion.infra.InterviewQuestionRepository;
import com.mock.interview.global.auditing.BaseEntity;
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
    @OneToMany(mappedBy = "question")
    private List<QuestionTechLink> techLink = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_question_id")
    private InterviewQuestion parentQuestion;

    @Cascade(CascadeType.ALL)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_token_id")
    private QuestionTokenization questionToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "related_experience_id")
    private Experience experience;

    private boolean isHidden;

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
        question.reveal();

        repository.save(question);
        Events.raise(new QuestionCreatedEvent(question.getId()));
        return question;
    }

    public void linkCategory(JobCategory category) {
        this.category = category;
        this.position = null;
    }

    public void linkPosition(JobPosition position) {
        if(category == null || !position.getCategory().getId().equals(category.getId()))
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
        if (this.questionType != QuestionType.EXPERIENCE
                || this.getOwner() != experience.getUsers())
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

    public void hide() {
        isHidden = true;
    }

    public void reveal() {
        isHidden = false;
    }

    public void linkQuestionToken(QuestionTokenization tokenization) {
        this.questionToken = tokenization;
    }

    public void linkParent(InterviewQuestion parent) {
        if(parent.getId().equals(this.id))
            throw new IllegalArgumentException("자기 자신을 부모 질문으로 설정할 수 없음");
        if(parent.isHidden() && parent.getOwner() != this.owner)
            throw new IllegalStateException("비밀 질문입니다.");
        this.parentQuestion = parent;
    }

    public void removeParent() {
        if(parentQuestion == null)
            throw new IllegalStateException("상위 질문이 존재하지 않음");
        parentQuestion = null;
    }

    public void removeExperience() {
        if(experience == null)
            throw new IllegalStateException("연관 경험이 존재하지 않음");
        experience = null;
    }

    public void changeExperience(Experience experience) {
        if(experience == null)
            throw new IllegalStateException();
        if(experience.getUsers() != this.getOwner())
            throw new IllegalStateException("본인의 경험만 등록 가능");
        this.experience = experience;
    }

    public void changeQuestion(String question) {
        if(question == null)
            throw new IllegalArgumentException();
        this.question = question;
    }

    public void changeType(QuestionType type) {
        if(type == null)
            throw new IllegalArgumentException("질문 타입은 필수");
        this.questionType = type;
    }


    // TODO: 조회 수 추가하기.
    // TODO: 질문에 대한 댓글 추가
}