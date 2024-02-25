package com.mock.interview.temp;

import com.mock.interview.interview.domain.model.Interview;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InterviewConversationPair {
    @Id
    @Column(name = "interview_history_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Interview interview;

    @ManyToOne
    private InterviewQuestion question;

    @OneToOne
    private InterviewAnswer answer;
}
