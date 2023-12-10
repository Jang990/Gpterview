package com.mock.interview.infrastructure.interview.strategy;

import com.mock.interview.domain.Category;
import com.mock.interview.infrastructure.gpt.AISpecification;
import com.mock.interview.infrastructure.gpt.InterviewAIRequest;
import com.mock.interview.presentaion.web.dto.InterviewInfo;

/**
 * 인터뷰 타입에 따라 각 분야(ex IT, 회계, 영업)별 인터뷰 형식 설정.
 * IT의 경우 기술 면접을 깊게...
 * 영업의 경우 인성을 깊게...
 * AI에게 보낼 메시지 리스트 세팅.
 * return 값을 AIRequest에 전달해서 실제 요청.
 */
public interface InterviewerStrategy {
    /**
     * AI별로 토큰의 값이 다르기 때문에 적절히 자르는 과정이 필요.
     * @param aiSpec AI 스펙을 확인할 수 있는 인터페이스
     * @param interviewInfo 진행되는 인터뷰의 전체 정보
     * @return
     */
    InterviewAIRequest configStrategy(AISpecification aiSpec, InterviewInfo interviewInfo);

    boolean isSupportedCategory(Category category);
}
