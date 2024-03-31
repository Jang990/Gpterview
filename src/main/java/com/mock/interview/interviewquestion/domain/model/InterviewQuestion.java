package com.mock.interview.interviewquestion.domain.model;

import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.global.Events;
import com.mock.interview.interviewquestion.domain.event.QuestionCreatedEvent;
import com.mock.interview.interviewquestion.infra.InterviewQuestionRepository;
import com.mock.interview.interviewquestion.infra.ai.progress.InterviewPhase;
import com.mock.interview.global.auditing.BaseEntity;
import com.mock.interview.interviewanswer.domain.model.InterviewAnswer;
import com.mock.interview.interviewquestion.infra.RecommendedQuestion;
import com.mock.interview.interviewquestion.presentation.dto.QuestionTypeForView;
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
    @JoinColumn(name = "applied_job_id")
    private JobCategory appliedJob;

    @Column(nullable = false)
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

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "interviewQuestion")
    QuestionTokenization questionToken;

    private static InterviewQuestion createWithCommonField(
            InterviewQuestionRepository repository, String content, JobCategory category, Users users,
            QuestionType questionType, String createdBy
    ) {
        InterviewQuestion question = new InterviewQuestion();
        question.question = content;
        question.appliedJob = category;
        question.owner = users;
        question.questionType = questionType;
        question.createdBy = createdBy;
        question.likes = 0;

        repository.save(question);
        Events.raise(new QuestionCreatedEvent(question.getId()));
        return question;
    }

    public static InterviewQuestion createConversationQuestion(
            InterviewQuestionRepository repository,
            Users owner, JobCategory appliedJob,
            RecommendedQuestion questionInfo,
            List<TechnicalSubjects> techList
    ) {
        InterviewQuestion question = createWithCommonField(
                repository, questionInfo.question(), appliedJob, owner,
                convert(questionInfo.progress().phase()), questionInfo.createdBy()
        );
        connectTech(techList, question);
        return question;
    }

    public static InterviewQuestion create(InterviewQuestionRepository repository, String content, QuestionTypeForView type, JobCategory category, List<TechnicalSubjects> techList, Users users) {
        InterviewQuestion question = createWithCommonField(
                repository, content, category,
                users, convert(type), users.getUsername()
        );
        connectTech(techList, question);

        return question;
    }

    private static void connectTech(List<TechnicalSubjects> techList, InterviewQuestion question) {
        if(techList != null)
            question.techLink = createTechLinks(question, techList);
    }

    private static List<QuestionTechLink> createTechLinks(InterviewQuestion question, List<TechnicalSubjects> techList) {
        return techList.stream().map((tech) -> QuestionTechLink.createLink(question, tech)).toList();
    }

    private static QuestionType convert(InterviewPhase phase) {
        return switch (phase) {
            case TECHNICAL -> QuestionType.TECHNICAL;
            case EXPERIENCE -> QuestionType.EXPERIENCE;
            case PERSONAL -> QuestionType.PERSONALITY;
        };
    }

    private static QuestionType convert(QuestionTypeForView type) {
        return switch (type) {
            case TECHNICAL -> QuestionType.TECHNICAL;
            case EXPERIENCE -> QuestionType.EXPERIENCE;
            case PERSONALITY -> QuestionType.PERSONALITY;
        };
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
