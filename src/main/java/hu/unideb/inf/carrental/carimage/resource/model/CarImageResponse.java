package hu.unideb.inf.carrental.carimage.resource.model;

import java.util.Base64;

public class CarImageResponse {
    private String data;

    public CarImageResponse() {
    }

    public CarImageResponse(byte[] data) {
        this.data = Base64.getEncoder().encodeToString(data);
    }

    public String getData() {
        return data;
    }
}
