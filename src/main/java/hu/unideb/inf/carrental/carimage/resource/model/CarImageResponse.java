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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CarImageResponse that = (CarImageResponse) o;

        return data.equals(that.data);
    }

    @Override
    public int hashCode() {
        return data.hashCode();
    }
}
