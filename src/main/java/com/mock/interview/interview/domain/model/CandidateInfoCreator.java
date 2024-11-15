package com.mock.interview.interview.domain.model;

import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.category.domain.model.JobPosition;
import com.mock.interview.user.domain.model.Users;
import org.springframework.stereotype.Service;

@Service
public class CandidateInfoCreator {
    public CandidateInfo create(Users users, JobCategory category, JobPosition position) {
        // TODO: position은 가끔 없어도 되지 않나?
        if(users == null || category == null || position == null)
            throw new IllegalArgumentException("지원자 정보 누락");
        if(!position.isRelatedTo(category))
            throw new IllegalArgumentException("카테고리와 관계없는 포지션입니다.");

        return new CandidateInfo(users, category, position);
    }
}
