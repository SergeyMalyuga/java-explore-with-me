package ru.practicum.exploreWithMe.hit.exception;

public class ErrorResponseService {
    private String error;
    private String description;

    public ErrorResponseService(String error, String description) {
        this.error = error;
        this.description = description;
    }

    public String getError() {
        return error;
    }

    public String getDescription() {
        return description;
    }
}
