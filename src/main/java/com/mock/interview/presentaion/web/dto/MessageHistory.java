package com.mock.interview.presentaion.web.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 면접자와 면접관이 나눈 대화 기록
 */
@Data
@NoArgsConstructor
public class MessageHistory {
    private List<Message> messages;
}
