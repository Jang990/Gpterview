package com.mock.interview.interview.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterviewInfoDto {
    private long id;
    private LocalDateTime expiredTime;
}
