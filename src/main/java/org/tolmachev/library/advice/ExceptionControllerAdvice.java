package org.tolmachev.library.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.tolmachev.library.exceptions.SubscriptionNotFoundException;
import org.tolmachev.library.model.ErrorMessage;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler({
            SubscriptionNotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleConcurrencyException(RuntimeException ex) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setError(ex.getMessage());
        return errorMessage;
    }
}
