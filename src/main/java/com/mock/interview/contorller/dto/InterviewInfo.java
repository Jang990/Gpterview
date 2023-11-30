package com.mock.interview.contorller.dto;

import lombok.*;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
public class InterviewInfo {
    private MessageHistory messageHistory;
    private CandidateProfileDTO profile;

    public static InterviewInfo testCreator(String position, String field) {
        InterviewInfo info = new InterviewInfo();
        info.setMessageHistory(new MessageHistory());
        info.setProfile(new CandidateProfileDTO());

        info.getMessageHistory().setMessages(new ArrayList<>());
        info.getProfile().setField(field);
        info.getProfile().setPosition(position);

        return info;
    }
}
