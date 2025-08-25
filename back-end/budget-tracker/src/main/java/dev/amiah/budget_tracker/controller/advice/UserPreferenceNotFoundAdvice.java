package dev.amiah.budget_tracker.controller.advice;

import dev.amiah.budget_tracker.exception.UserPreferenceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserPreferenceNotFoundAdvice {

    @ExceptionHandler(UserPreferenceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String userPreferenceNotFoundHandler(UserPreferenceNotFoundException e) {
        return e.getMessage();
    }

}
