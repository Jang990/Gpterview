package com.mock.interview.interview.infrastructure.interview.gpt;

import com.mock.interview.interview.infrastructure.interview.dto.Message;

/**
 * AI별 인터뷰 Requester
 * 이 AI API를 사용해서 전달하는데 최선을 다할 것.
 * 면접에 대한 관여는 최소한으로 할 것. (ex- history 변경)
 * 면접 관련 부분은 전략에서 다 예외처리를 할 것이다.
 */
public interface AIRequester extends AISpecification {
    /**
     * AI API를 사용하여 실제 Request를 보내는 메소드
     * @param request InterviewStrategy로 받은 결과를 넣어줄 것.
     *                history를 직접 변경하는 것에 주의할 것.
     *                다른 곳에 영향을 줄 수 있음. 써야한다면 Clone해서 깊은 복사로 사용할 것.
     * @return Request로 받은 Response 메시지
     */
    Message sendRequest(InterviewAIRequest request);
}
