package com.mock.interview.interviewquestion.infra.ai.prompt.configurator.generator;

import com.mock.interview.interviewquestion.infra.ai.dto.InterviewInfo;
import com.mock.interview.interviewquestion.infra.ai.dto.InterviewProfile;
import com.mock.interview.interviewquestion.infra.ai.prompt.configurator.PromptConfiguration;
import com.mock.interview.interviewquestion.infra.ai.gpt.AISpecification;
import com.mock.interview.interviewquestion.infra.ai.progress.InterviewProgress;

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

    boolean isSupportedDepartment(InterviewInfo interviewInfo);
}
