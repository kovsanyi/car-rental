package hu.unideb.inf.carrental.commons.model;

import hu.unideb.inf.carrental.commons.exception.enumeration.ExceptionType;

public class ErrorResponse {
    private String status;
    private String error;
    private String message;

    public ErrorResponse(ExceptionType error, String message) {
        this.status = "failed";
        this.error = error.toString();
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}
