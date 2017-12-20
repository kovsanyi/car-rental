package hu.unideb.inf.carrental.commons.model;

public class SuccessResponse {
    private String status;

    public SuccessResponse() {
        this.status = "ok";
    }

    public String getStatus() {
        return status;
    }
}
