package com.mock.interview.interview.domain;

import com.mock.interview.global.Events;
import com.mock.interview.interview.domain.exception.IsAlreadyTimeoutInterviewException;
import com.mock.interview.interview.domain.exception.NotEnoughRelatedQuestionException;
import com.mock.interview.interview.domain.model.Interview;
import org.springframework.stereotype.Service;

@Service
public class InterviewRecommendedService {
    private final int MIN_RELATED_QUESTION_SIZE = 30;
    public void recommended(Interview interview, int relatedQuestionSize) {
        if(interview.isTimeout())
            throw new IsAlreadyTimeoutInterviewException();
        if(relatedQuestionSize < MIN_RELATED_QUESTION_SIZE)
            throw new NotEnoughRelatedQuestionException();

        Events.raise(new RecommendationRequestedEvent(interview.getId()));
    }
}
