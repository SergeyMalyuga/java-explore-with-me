package ru.practicum.exploreWithMe.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ErrorResponseMain {
    private StackTraceElement[] errors;
    private String message;
    private String reason;
    private String status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public ErrorResponseMain(StackTraceElement[] errors, String message, String reason, String status, LocalDateTime timestamp) {
        this.errors = errors;
        this.message = message;
        this.reason = reason;
        this.status = status;
        this.timestamp = timestamp;
    }
}
