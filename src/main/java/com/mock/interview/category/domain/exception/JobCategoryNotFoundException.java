package com.mock.interview.category.domain.exception;

import com.mock.interview.global.exception.CustomClientException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class JobCategoryNotFoundException extends CustomClientException {
    public JobCategoryNotFoundException() {
        super(HttpStatus.NOT_FOUND, "not.found.jobcategory");
    }

    public JobCategoryNotFoundException(Throwable cause) {
        super(cause, HttpStatus.NOT_FOUND, "not.found.jobcategory");
    }
}
