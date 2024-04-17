package com.mock.interview.global;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public final class RepositoryConst {
    public static final Pageable LIMIT_ONE = PageRequest.of(0, 1);
    private RepositoryConst() {}
}
