package com.mock.interview.interviewquestion.domain.model;

import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.conversation.infrastructure.interview.strategy.stage.InterviewStage;
import com.mock.interview.global.auditing.BaseEntity;
import com.mock.interview.interviewanswer.domain.model.InterviewAnswer;
import com.mock.interview.interviewquestion.infra.PublishedQuestionInfo;
import com.mock.interview.user.domain.model.Users;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 면접이라는 기능에 초점을 맞추지 말고, 면접 질문 게시판에 초점을 맞추는게 맞는 것 같다.
 * 면접은 다른 사용자들이 만들어 놓은 면접 질문을 추천하거나, AI를 통해 실시간으로 만들어 주는 기능일 뿐이다.
 */
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

    /*
    내용, 분야, 필드
    추천수, 조회수
    생성한사람(AI-GPT or 사용자이름), Owner(사용자ID)
     */

    public static InterviewQuestion createInInterview(
            Users owner, JobCategory appliedJob,
            PublishedQuestionInfo questionInfo
    ) {
        InterviewQuestion question = new InterviewQuestion();
        question.owner = owner;
        question.appliedJob = appliedJob;
        question.createdBy = questionInfo.createdBy();
        question.lastModifiedBy = questionInfo.createdBy();
        question.question = questionInfo.question();
        question.questionType = convert(questionInfo.progress().stage());
        return question;
    }

    private static QuestionType convert(InterviewStage stage) {
        return switch (stage) {
            case TECHNICAL -> QuestionType.TECHNICAL;
            case EXPERIENCE -> QuestionType.EXPERIENCE;
            case PERSONAL -> QuestionType.PERSONALITY;
        };
    }

    // TODO: 질문 게시판 추가
    // TODO: boolean 공개여부 추가
    // TODO: 추천 수, 조회 수 추가하기.
    // TODO: 질문에 대한 댓글 추가
}
