package hu.unideb.inf.carrental.car.resource.model;

import hu.unideb.inf.carrental.commons.constant.Constants;
import hu.unideb.inf.carrental.commons.domain.car.enumeration.CarCategory;
import hu.unideb.inf.carrental.commons.domain.car.enumeration.FuelType;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class CreateCarRequest {
    @NotNull
    private Long siteId;

    @NotNull
    private CarCategory category;

    @NotBlank
    @Length(min = Constants.Car.BRAND_MIN_LENGTH,
            max = Constants.Car.BRAND_MAX_LENGTH)
    private String brand;

    @NotBlank
    @Length(min = Constants.Car.MODEL_MIN_LENGTH,
            max = Constants.Car.MODEL_MAX_LENGTH)
    private String model;

    @NotNull
    @Max(Constants.Car.TRUNK_CAPACITY_MAX_VALUE)
    private Integer trunkCapacity;

    @NotNull
    @Min(Constants.Car.YEAR_MIN_VALUE)
    @Max(Constants.Car.YEAR_MAX_VALUE)
    private Integer year;

    @NotNull
    private FuelType fuelType;

    @NotNull
    private Float fuelConsumption;

    @NotNull
    @Max(Constants.Car.TANK_CAPACITY_MAX_VALUE)
    private Integer tankCapacity;

    @NotNull
    @Max(Constants.Car.SEAT_NUMBER_MAX_VALUE)
    private Integer seatNumber;

    @NotNull
    @Max(Constants.Car.PRICE_MAX_VALUE)
    private Integer price;

    public CreateCarRequest() {
    }

    public CreateCarRequest(Long siteId, CarCategory category, String brand, String model, Integer trunkCapacity,
                            Integer year, FuelType fuelType, Float fuelConsumption, Integer tankCapacity,
                            Integer seatNumber, Integer price) {
        this.siteId = siteId;
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

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public CarCategory getCategory() {
        return category;
    }

    public void setCategory(CarCategory category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getTrunkCapacity() {
        return trunkCapacity;
    }

    public void setTrunkCapacity(Integer trunkCapacity) {
        this.trunkCapacity = trunkCapacity;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    public Float getFuelConsumption() {
        return fuelConsumption;
    }

    public void setFuelConsumption(Float fuelConsumption) {
        this.fuelConsumption = fuelConsumption;
    }

    public Integer getTankCapacity() {
        return tankCapacity;
    }

    public void setTankCapacity(Integer tankCapacity) {
        this.tankCapacity = tankCapacity;
    }

    public Integer getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(Integer seatNumber) {
        this.seatNumber = seatNumber;
    }

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

        CreateCarRequest that = (CreateCarRequest) o;

        if (!siteId.equals(that.siteId)) return false;
        if (category != that.category) return false;
        if (!brand.equals(that.brand)) return false;
        if (!model.equals(that.model)) return false;
        if (!trunkCapacity.equals(that.trunkCapacity)) return false;
        if (!year.equals(that.year)) return false;
        if (fuelType != that.fuelType) return false;
        if (!fuelConsumption.equals(that.fuelConsumption)) return false;
        if (!tankCapacity.equals(that.tankCapacity)) return false;
        if (!seatNumber.equals(that.seatNumber)) return false;
        return price.equals(that.price);
    }

    @Override
    public int hashCode() {
        int result = siteId.hashCode();
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
        return "CreateCarRequest{" +
                "siteId=" + siteId +
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
