package com.mock.interview.global;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.util.Locale;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ApiExceptionHandler {
    private final MessageSource messageSource;

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> bindingExceptionHandler(BindException e, Locale locale) {
        StringBuilder errorStringBuilder = new StringBuilder();
        for (ObjectError errors : e.getAllErrors()) {
            String errorMessage = messageSource.getMessage(errors, LocaleContextHolder.getLocale());
            errorStringBuilder.append(errorMessage).append("\n");
        }
        return new ResponseEntity<>(new ErrorResponse(errorStringBuilder.toString()), HttpStatus.BAD_REQUEST);
    }
}
