package hu.unideb.inf.carrental.commons.model;

public class CreatedResponse {
    private String status;
    private Long id;

    public CreatedResponse(Long id) {
        status = "ok";
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public Long getId() {
        return id;
    }
}
