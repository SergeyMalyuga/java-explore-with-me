package ru.practicum.exploreWithMe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse noDataFound(NoDataFoundException e) {
        return new ErrorResponse(e.getStackTrace(), e.getMessage(), "The required object was not found.",
                HttpStatus.NOT_FOUND.toString(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse invalidDate(InvalidDateException e) {
        return new ErrorResponse(e.getStackTrace(), e.getMessage(),
                "For the requested operation the conditions are not met.",
                HttpStatus.FORBIDDEN.toString(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse accessError(AccessErrorException e) {
        return new ErrorResponse(e.getStackTrace(), e.getMessage(),
                "Data access error.",
                HttpStatus.NOT_FOUND.toString(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse eventUserUpdateError(EventUserUpdateException e) {
        return new ErrorResponse(e.getStackTrace(), e.getMessage(),
                "Update error.",
                HttpStatus.NOT_FOUND.toString(), LocalDateTime.now());
    }

}

