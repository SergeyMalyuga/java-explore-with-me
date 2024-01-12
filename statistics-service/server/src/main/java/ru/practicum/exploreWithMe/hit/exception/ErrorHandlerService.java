package ru.practicum.exploreWithMe.hit.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandlerService {

    @ExceptionHandler
    public ErrorResponseService invalidDate(InvalidDataException e) {
        return new ErrorResponseService(e.getMessage(), "Ошибка ввода даты/времени.");
    }
}
