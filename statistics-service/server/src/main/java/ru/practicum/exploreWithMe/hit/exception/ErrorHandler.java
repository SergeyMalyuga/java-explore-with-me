package ru.practicum.exploreWithMe.hit.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    public ErrorResponse invalidDate(InvalidDataException e) {
        return new ErrorResponse(e.getMessage(), "Ошибка ввода даты/времени.");
    }
}
