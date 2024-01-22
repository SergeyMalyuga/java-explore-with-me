package ru.practicum.exploreWithMe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorHandlerMain {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseMain noDataFound(NoDataFoundException e) {
        return new ErrorResponseMain(e.getStackTrace(), e.getMessage(), "The required object was not found.",
                HttpStatus.NOT_FOUND.toString(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseMain invalidDate(InvalidDateException e) {
        return new ErrorResponseMain(e.getStackTrace(), e.getMessage(),
                "For the requested operation the conditions are not met.",
                HttpStatus.BAD_REQUEST.toString(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseMain accessError(AccessErrorException e) {
        return new ErrorResponseMain(e.getStackTrace(), e.getMessage(),
                "Data access error.",
                HttpStatus.NOT_FOUND.toString(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponseMain eventUserUpdateError(EventUserUpdateException e) {
        return new ErrorResponseMain(e.getStackTrace(), e.getMessage(),
                "Update error.",
                HttpStatus.CONFLICT.toString(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponseMain repeatRequestError(RequestException e) {
        return new ErrorResponseMain(e.getStackTrace(), e.getMessage(),
                "Integrity constraint has been violated.",
                HttpStatus.CONFLICT.toString(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponseMain commentAddError(CommentAddException e) {
        return new ErrorResponseMain(e.getStackTrace(), e.getMessage(),
                "Duplicate.",
                HttpStatus.CONFLICT.toString(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponseMain commentDeleteError(CommentDeleteException e) {
        return new ErrorResponseMain(e.getStackTrace(), e.getMessage(),
                "Delete error.",
                HttpStatus.CONFLICT.toString(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponseMain commentUpdateError(CommentUpdateException e) {
        return new ErrorResponseMain(e.getStackTrace(), e.getMessage(),
                "Duplicate.",
                HttpStatus.CONFLICT.toString(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseMain notBlankError(ConstraintViolationException e) {
        return new ErrorResponseMain(e.getStackTrace(), e.getMessage(),
                "NotBlank exception.",
                HttpStatus.BAD_REQUEST.toString(), LocalDateTime.now());
    }

}

