package com.mock.interview.interview.domain;

import com.mock.interview.global.auditing.BaseTimeEntity;
import com.mock.interview.interview.domain.exception.IsAlreadyTimeoutInterviewException;
import com.mock.interview.interview.presentation.dto.InterviewDetailsDto;
import com.mock.interview.interview.presentation.dto.InterviewType;
import com.mock.interview.user.domain.CandidateProfile;
import com.mock.interview.user.domain.Users;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Interview extends BaseTimeEntity {
    @Id
    @Column(name = "interview_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;

    @Column(nullable = false)
    private boolean isActive;
    private boolean isDeleted;

    private int durationMinutes;
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private InterviewType type; // TODO: DB에 넣어야 할 듯?

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_profile_id")
    private CandidateProfile candidateProfile;

    public static Interview startInterview(InterviewDetailsDto interviewSetting, Users user, CandidateProfile profile) {
        Interview interview = new Interview();
        LocalDateTime now = LocalDateTime.now();
        interview.title = createTimeTitle(interview, now);
        interview.endTime = now.plusMinutes(interviewSetting.getDurationMinutes());
        interview.type = interviewSetting.getInterviewType();
        interview.durationMinutes = interviewSetting.getDurationMinutes();
        interview.isActive = true;
        interview.isDeleted = false;
        interview.users = user;
        interview.candidateProfile = profile;

        return interview;
    }

    private static String createTimeTitle(Interview interview, LocalDateTime now) {
        // TODO: DateTimeFormatter 상수로 빼기
        return DateTimeFormatter.ofPattern("yy.MM.dd HH시 mm분 면접").format(now);
    }

    public boolean isActive() {
        return LocalDateTime.now().isBefore(endTime);
    }

    public void timeout() {
        if(!this.isActive)
            throw new IsAlreadyTimeoutInterviewException();

        this.isActive = false;
    }
}
