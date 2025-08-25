package dev.amiah.budget_tracker.controller.advice;

import dev.amiah.budget_tracker.exception.ExpenseNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExpenseNotFoundAdvice {

    @ExceptionHandler(ExpenseNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String expenseNotFoundHandler(ExpenseNotFoundException e) {
        return e.getMessage();
    }

}
