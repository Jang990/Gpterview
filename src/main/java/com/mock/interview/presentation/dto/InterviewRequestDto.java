package com.mock.interview.presentation.dto;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterviewRequestDto {
    // TODO:  나중에 DB 등 저장 및 로그인 기능 넣으면서 서버에서 관리할 것.
    private InterviewSettingDto interviewSetting;
    @Valid
    private MessageHistoryDto messageHistory;
}
