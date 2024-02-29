package com.mock.interview.interview.presentation.dto;

import com.mock.interview.candidate.presentation.dto.InterviewCandidateForm;
import com.mock.interview.interview.presentation.dto.message.MessageHistoryDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterviewRequestDto {
    // TODO:  나중에 DB 등 저장 및 로그인 기능 넣으면서 서버에서 관리할 것.
    private InterviewCandidateForm interviewSetting;
    @Valid
    private MessageHistoryDto messageHistory;
}
