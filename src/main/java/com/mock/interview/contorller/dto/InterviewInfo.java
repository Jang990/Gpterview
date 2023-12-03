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
        info.getProfile().setDepartment(field);
        info.getProfile().setField(position);

        MessageHistory history = new MessageHistory();
        history.setMessages(new ArrayList<>());
        history.getMessages().add(new Message("system", "너는 개발 분야 백엔드 포지션의 면접관이고 user는 면접 지원자야. 너는 질문하고 user는 대답한다.너는 user의 대답에 이상한 부분이 있다면 추궁할 수 있다.넌 한 번에 응답에 한 번의 질문을 한다."));
        history.getMessages().add(new Message("assistant", "안녕하세요. 백엔드 개발 분야에 지원한 이유가 무엇인가요?"));
        info.setMessageHistory(history);

        return info;
    }
}
