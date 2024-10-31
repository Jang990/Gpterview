package com.mock.interview.interview;

import com.mock.interview.interview.domain.InterviewTimeHolder;
import com.mock.interview.interview.domain.exception.InterviewNotFoundException;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.infra.InterviewRepository;
import com.mock.interview.interview.infra.cache.InterviewCacheRepository;
import com.mock.interview.interview.infra.lock.progress.InterviewProgressLock;
import com.mock.interview.interview.infra.lock.progress.dto.InterviewUserIds;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InterviewExpirationService {
    private final InterviewTimeHolder interviewTimeHolder;

    private final InterviewRepository repository;
    private final InterviewCacheRepository interviewCacheRepository;

    @InterviewProgressLock
    public void expire(InterviewUserIds lockDto) {
        Interview interview = repository.findByIdAndUserId(lockDto.getInterviewId(), lockDto.getUserId())
                .orElseThrow(InterviewNotFoundException::new);
        interview.expire(interviewTimeHolder);
        interviewCacheRepository.expireInterviewInfo(lockDto.getInterviewId());
    }
}
