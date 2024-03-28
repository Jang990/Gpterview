package com.mock.interview.interview.application;

import com.mock.interview.interview.domain.InterviewRecommendedService;
import com.mock.interview.interview.domain.exception.InterviewNotFoundException;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.infra.InterviewRepository;
import com.mock.interview.interviewquestion.infra.InterviewQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecommendedQuestionService {
    private final InterviewQuestionRepository questionRepository;
    private final InterviewRepository interviewRepository;
    private final InterviewRecommendedService recommendedService;

    @Transactional(readOnly = true)
    public void recommend(long loginId, long interviewId) {
        Interview interview = interviewRepository.findInterviewSetting(interviewId, loginId)
                .orElseThrow(InterviewNotFoundException::new);


        // TODO: 임시 코드 QuestionRepo에서 Count쿼리를 날릴 것. => cache로 최적화.
        recommendedService.recommended(interview, 100);
//        List<QuestionMetaData> questionMetaDataList = questionForRecommend.stream().map(question -> convertQuestion(question)).toList();
    }
}
