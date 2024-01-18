package com.mock.interview.user.domain.model;

import com.mock.interview.user.domain.UsersConst;
import com.mock.interview.user.domain.exception.DailyInterviewLimitExceededException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private long dailyInterviewUsage;

    public static Users createUser(String username, String password) {
        Users user = new Users();
        user.username = username;
        user.password = password;
        user.dailyInterviewUsage = 0;
        return user;
    }

    public void increaseInterviewUsage() {
        if(dailyInterviewUsage <= UsersConst.DAILY_INTERVIEW_USAGE)
            throw new DailyInterviewLimitExceededException();
        dailyInterviewUsage++;
    }
}
