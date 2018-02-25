package hu.unideb.inf.carrental.commons.domain.carimage;

import hu.unideb.inf.carrental.commons.constant.Constants;
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
    @Column(length = Constants.CarImage.MAX_SIZE)
    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CarImage carImage = (CarImage) o;

        if (id != null ? !id.equals(carImage.id) : carImage.id != null) return false;
        return car.equals(carImage.car);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + car.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "CarImage{" +
                "id=" + id +
                ", carID=" + car.getId() +
                ", imageDataSize=" + imageData.length +
                '}';
    }
}
