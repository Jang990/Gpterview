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

    public static MessageDto createQuestion(long id, String content) {
        return new MessageDto(id, InterviewRole.AI, content);
    }

    public static MessageDto createAnswer(long id, String content) {
        return new MessageDto(id, InterviewRole.USER, content);
    }
}
