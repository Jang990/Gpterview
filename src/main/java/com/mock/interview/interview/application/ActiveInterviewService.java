package com.mock.interview.interview.application;

import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.infra.InterviewRepository;
import com.mock.interview.interview.presentation.dto.InterviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ActiveInterviewService {
    private final InterviewRepository repository;
    @Transactional(readOnly = true)
    public Optional<InterviewResponse> find(long userId) {
        Optional<Interview> optionalInterview = repository.findActiveInterview(userId);
        if(optionalInterview.isEmpty())
            return Optional.empty();

        return Optional.of(convert(optionalInterview.get()));
    }

    private InterviewResponse convert(Interview activeInterview) {
        return new InterviewResponse(activeInterview.getId(), activeInterview.getTitle().getTitle());
    }
}
