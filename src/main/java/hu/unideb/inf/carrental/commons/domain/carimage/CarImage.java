package hu.unideb.inf.carrental.commons.domain.carimage;

import hu.unideb.inf.carrental.commons.domain.car.Car;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class CarImage {
    private Long id;
    private Car car;
    private byte[] imageData;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    @ManyToOne
    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    @NotNull
    @Column(length = 2000000)
    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }
}
