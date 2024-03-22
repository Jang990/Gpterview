package com.mock.interview.interview.presentation.dto.message;

import com.mock.interview.interview.presentation.dto.InterviewRole;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    private Long id;
    private InterviewRole role;

    @Size(min = 3, max = 200)
    private String content;
}
