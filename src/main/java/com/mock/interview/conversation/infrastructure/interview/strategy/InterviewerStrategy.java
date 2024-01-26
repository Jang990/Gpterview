package com.mock.interview.conversation.infrastructure.interview.strategy;

import com.mock.interview.conversation.infrastructure.interview.dto.InterviewInfo;
import com.mock.interview.conversation.infrastructure.interview.dto.MessageHistory;
import com.mock.interview.conversation.infrastructure.interview.gpt.AISpecification;
import com.mock.interview.conversation.infrastructure.interview.gpt.InterviewAIRequest;
import com.mock.interview.conversation.infrastructure.interview.setting.InterviewSetting;

/**
 * 인터뷰 정보에 따라 각 분야(ex IT, 회계, 영업)별 AI에게 전달할 프롬프트 생성기
 *
 * 다음 상황을 컨트롤 해줘야함.
 * IT의 경우 기술 면접 위주...
 * 영업의 경우 인성을 깊게...
 *
 */
public interface InterviewerStrategy {
    /**
     * 인터뷰 전략에 맞는 프롬프트 생성
     *
     * @param aiSpec AI 스펙을 확인할 수 있는 인터페이스
     * @param interviewInfo 진행되는 인터뷰 지원자의 정보
     * @return
     */
    InterviewSetting configStrategy(AISpecification aiSpec, InterviewInfo interviewInfo);

    /**
     * 인터뷰 전략에 맞춰 현재 사용자의 주제를 변경하는 프롬프트 생성
     *
     * @param aiSpec AI 스펙을 확인할 수 있는 인터페이스
     * @param interviewInfo 진행되는 인터뷰 지원자의 정보
     * @return
     */
    InterviewSetting changeTopic(AISpecification aiSpec, InterviewInfo interviewInfo);

    boolean isSupportedDepartment(InterviewInfo interviewInfo);
}
