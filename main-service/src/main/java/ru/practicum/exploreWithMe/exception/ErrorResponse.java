package ru.practicum.exploreWithMe.exception;

import java.time.LocalDateTime;

public class ErrorResponse {
    private StackTraceElement[] errors;
    private String message;
    private String reason;
    private String status;
    private LocalDateTime timestamp;

    public ErrorResponse(StackTraceElement[] errors, String message, String reason, String status, LocalDateTime timestamp) {
        this.errors = errors;
        this.message = message;
        this.reason = reason;
        this.status = status;
        this.timestamp = timestamp;
    }
}
