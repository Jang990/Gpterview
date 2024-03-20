package com.mock.interview.interviewquestion.infra.interview.strategy;

import com.mock.interview.interviewquestion.infra.interview.dto.InterviewInfo;
import com.mock.interview.interviewquestion.infra.interview.dto.InterviewProfile;
import com.mock.interview.interviewquestion.infra.interview.dto.PromptConfiguration;
import com.mock.interview.interviewquestion.infra.interview.gpt.AISpecification;
import com.mock.interview.interviewquestion.infra.interview.setting.AiPrompt;
import com.mock.interview.interviewquestion.infra.interview.strategy.stage.InterviewProgress;

/**
 * 인터뷰 정보에 따라 각 분야(ex IT, 회계, 영업)별 관련 프롬프트 정보 생성기
 *
 * 진행도에 따른 PromptCreationInfo를 생성
 * 진행도에 따른 분야별 인터뷰 흐름 핸들링
 * ex) IT의 경우 기술 면접 시 CS 지식도 질문.
 */
public interface InterviewPromptConfigurator {
    /**
     * 인터뷰 전략에 맞는 프롬프트 생성 정보 반환
     *
     * @param aiSpec AI 스펙을 확인할 수 있는 인터페이스
     * @param profile 지원자 정보
     * @param progress 면접 진행 정도
     * @return
     */
    PromptConfiguration configStrategy(AISpecification aiSpec, InterviewProfile profile, InterviewProgress progress);

    /**
     * 인터뷰 전략에 맞춰 현재 사용자의 주제를 변경하는 프롬프트 생성
     *
     * @param aiSpec AI 스펙을 확인할 수 있는 인터페이스
     * @param interviewInfo 진행되는 인터뷰 지원자의 정보
     * @return
     */
    AiPrompt changeTopic(AISpecification aiSpec, InterviewInfo interviewInfo);

    boolean isSupportedDepartment(InterviewInfo interviewInfo);
}
