package com.mock.interview.interview.infra.prompt.configurator.generator;

import com.mock.interview.category.infra.support.CategorySupportChecker;
import com.mock.interview.interview.infra.cache.dto.InterviewProfile;
import com.mock.interview.interview.infra.progress.dto.InterviewProgress;
import com.mock.interview.interview.infra.prompt.configurator.PromptConfiguration;

/**
 * 인터뷰 정보에 따라 각 분야(ex IT, 회계, 영업)별 관련 프롬프트 정보 생성기
 *
 * 진행도에 따른 PromptCreationInfo를 생성
 * 진행도에 따른 분야별 인터뷰 흐름 핸들링
 * ex) IT의 경우 기술 면접 시 CS 지식도 질문.
 */
public interface InterviewPromptConfigurator extends CategorySupportChecker {
    /**
     * 인터뷰 전략에 맞는 프롬프트 생성 정보 반환
     *
     * @param profile 지원자 정보
     * @param progress 면접 진행 정도
     * @return
     */
    PromptConfiguration configStrategy(InterviewProfile profile, InterviewProgress progress);
}
