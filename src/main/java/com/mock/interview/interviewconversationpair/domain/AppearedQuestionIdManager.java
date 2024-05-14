
package com.mock.interview.interviewconversationpair.domain;

import java.util.List;

public interface AppearedQuestionIdManager {
    void appear(long interviewId, long questionId);
    List<Long> find(long interviewId);
    void delete(long interviewId);
}
