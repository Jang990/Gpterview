package com.mock.interview.interviewconversationpair.infra;

import com.mock.interview.global.TimeDifferenceCalculator;
import com.mock.interview.interview.domain.exception.InterviewNotFoundException;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.infra.InterviewRepository;
import com.mock.interview.interviewconversationpair.domain.AppearedQuestionIdManager;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AppearedQuestionIdManagerImpl implements AppearedQuestionIdManager {
    private final InterviewRepository interviewRepository;
    private final InterviewConversationPairRepository interviewConversationPairRepository;

    private final String KEY_FORMAT = "INTERVIEW:%d:QUESTIONS";
    private final RedisTemplate<String, AppearedQuestionIds> appearedQuestionRedisTemplate;

    public void appear(long interviewId, long questionId) {
        String key = createKey(interviewId);

        List<Long> ids = find(interviewId);
        ids.add(questionId);

        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(InterviewNotFoundException::new);
        save(interview, key, ids);
    }

    public List<Long> find(long interviewId) {
        String key = createKey(interviewId);
        AppearedQuestionIds cacheIds = appearedQuestionRedisTemplate.opsForValue().get(key);
        System.out.println(cacheIds);
        if (cacheIds != null)
            return cacheIds.getIds();

        List<Long> ids = findAppearedQuestionIds(interviewId);

        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(InterviewNotFoundException::new);
        save(interview, key, ids);
        return ids;
    }

    private List<Long> findAppearedQuestionIds(long interviewId) {
        List<Long> ids = interviewConversationPairRepository
                .findAppearedQuestionIds(interviewId);
        if(ids.isEmpty() || (ids.size() == 1 && ids.get(0) == null))
            return new LinkedList<>();
        return ids;
    }

    public void delete(long interviewId) {
        String key = createKey(interviewId);
        appearedQuestionRedisTemplate.delete(key);
    }

    private void save(Interview interview, String key, List<Long> ids) {
        long diffSecond = TimeDifferenceCalculator.calculate(
                ChronoUnit.SECONDS, LocalDateTime.now(), interview.getExpiredTime());
        if(diffSecond < 0)
            return;

        appearedQuestionRedisTemplate.opsForValue()
                .set(key, new AppearedQuestionIds(ids), Duration.ofSeconds(diffSecond));
    }

    private String createKey(long interviewId) {
        return String.format(KEY_FORMAT, interviewId);
    }

    @Getter
    @NoArgsConstructor
    static class AppearedQuestionIds {
        private List<Long> ids;

        public AppearedQuestionIds(List<Long> ids) {
            this.ids = ids;
        }
    }
}
