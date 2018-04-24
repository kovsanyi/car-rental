package hu.unideb.inf.carrental.commons.domain.car;

import hu.unideb.inf.carrental.commons.constant.Constants;
import hu.unideb.inf.carrental.commons.domain.car.enumeration.CarCategory;
import hu.unideb.inf.carrental.commons.domain.car.enumeration.FuelType;
import hu.unideb.inf.carrental.commons.domain.site.Site;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
public class Car {
    private Long id;
    private Site site;
    private CarCategory category;
    private String brand;
    private String model;
    private Integer trunkCapacity;
    private Integer year;
    private FuelType fuelType;
    private Float fuelConsumption;
    private Integer tankCapacity;
    private Integer seatNumber;
    private Integer price;

    public Car() {
    }

    public Car(Long id, Site site, CarCategory category, String brand, String model, Integer trunkCapacity, Integer year,
               FuelType fuelType, Float fuelConsumption, Integer tankCapacity, Integer seatNumber, Integer price) {
        this.id = id;
        this.site = site;
        this.category = category;
        this.brand = brand;
        this.model = model;
        this.trunkCapacity = trunkCapacity;
        this.year = year;
        this.fuelType = fuelType;
        this.fuelConsumption = fuelConsumption;
        this.tankCapacity = tankCapacity;
        this.seatNumber = seatNumber;
        this.price = price;
    }

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    @NotNull
    @Enumerated(value = EnumType.STRING)
    public CarCategory getCategory() {
        return category;
    }

    public void setCategory(CarCategory category) {
        this.category = category;
    }

    @NotBlank
    @Length(min = Constants.Car.BRAND_MIN_LENGTH,
            max = Constants.Car.BRAND_MAX_LENGTH)
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @NotBlank
    @Length(min = Constants.Car.MODEL_MIN_LENGTH,
            max = Constants.Car.MODEL_MAX_LENGTH)
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @NotNull
    @Max(Constants.Car.TRUNK_CAPACITY_MAX_VALUE)
    public Integer getTrunkCapacity() {
        return trunkCapacity;
    }

    public void setTrunkCapacity(Integer trunkCapacity) {
        this.trunkCapacity = trunkCapacity;
    }

    @NotNull
    @Min(Constants.Car.YEAR_MIN_VALUE)
    @Max(Constants.Car.YEAR_MAX_VALUE)
    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    @NotNull
    @Enumerated(value = EnumType.STRING)
    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    @NotNull
    public Float getFuelConsumption() {
        return fuelConsumption;
    }

    public void setFuelConsumption(Float fuelConsumption) {
        this.fuelConsumption = fuelConsumption;
    }

    @NotNull
    @Max(Constants.Car.TANK_CAPACITY_MAX_VALUE)
    public Integer getTankCapacity() {
        return tankCapacity;
    }

    public void setTankCapacity(Integer tankCapacity) {
        this.tankCapacity = tankCapacity;
    }

    @NotNull
    @Max(Constants.Car.SEAT_NUMBER_MAX_VALUE)
    public Integer getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(Integer seatNumber) {
        this.seatNumber = seatNumber;
    }

    @NotNull
    @Max(Constants.Car.PRICE_MAX_VALUE)
    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Car car = (Car) o;

        if (!site.equals(car.site)) return false;
        if (category != car.category) return false;
        if (!brand.equals(car.brand)) return false;
        if (!model.equals(car.model)) return false;
        if (!trunkCapacity.equals(car.trunkCapacity)) return false;
        if (!year.equals(car.year)) return false;
        if (fuelType != car.fuelType) return false;
        if (!fuelConsumption.equals(car.fuelConsumption)) return false;
        if (!tankCapacity.equals(car.tankCapacity)) return false;
        if (!seatNumber.equals(car.seatNumber)) return false;
        return price.equals(car.price);
    }

    @Override
    public int hashCode() {
        int result = site.hashCode();
        result = 31 * result + category.hashCode();
        result = 31 * result + brand.hashCode();
        result = 31 * result + model.hashCode();
        result = 31 * result + trunkCapacity.hashCode();
        result = 31 * result + year.hashCode();
        result = 31 * result + fuelType.hashCode();
        result = 31 * result + fuelConsumption.hashCode();
        result = 31 * result + tankCapacity.hashCode();
        result = 31 * result + seatNumber.hashCode();
        result = 31 * result + price.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", siteID=" + site.getId() +
                ", category=" + category +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", trunkCapacity=" + trunkCapacity +
                ", year=" + year +
                ", fuelType=" + fuelType +
                ", fuelConsumption=" + fuelConsumption +
                ", tankCapacity=" + tankCapacity +
                ", seatNumber=" + seatNumber +
                ", price=" + price +
                '}';
    }
}
