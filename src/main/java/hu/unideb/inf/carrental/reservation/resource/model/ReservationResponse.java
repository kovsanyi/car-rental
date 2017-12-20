package hu.unideb.inf.carrental.reservation.resource.model;

import hu.unideb.inf.carrental.commons.domain.car.enumeration.CarCategory;

public class ReservationResponse {
    private Long id;
    private String customerUserUsername;
    private String customerFullName;
    private String companyName;
    private Long carId;
    private CarCategory carCategory;
    private String carBrand;
    private String carModel;
    private String receiveDate;
    private String plannedReturnDate;
    private String returnedDate;
    private Integer price;

    public ReservationResponse() {
    }

    public ReservationResponse(Long id, String customerUserUsername, String customerFullName, String companyName, Long carId, CarCategory carCategory, String carBrand, String carModel, String receiveDate, String plannedReturnDate, String returnedDate, Integer price) {
        this.id = id;
        this.customerUserUsername = customerUserUsername;
        this.customerFullName = customerFullName;
        this.companyName = companyName;
        this.carId = carId;
        this.carCategory = carCategory;
        this.carBrand = carBrand;
        this.carModel = carModel;
        this.receiveDate = receiveDate;
        this.plannedReturnDate = plannedReturnDate;
        this.returnedDate = returnedDate;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerUserUsername() {
        return customerUserUsername;
    }

    public void setCustomerUserUsername(String customerUserUsername) {
        this.customerUserUsername = customerUserUsername;
    }

    public String getCustomerFullName() {
        return customerFullName;
    }

    public void setCustomerFullName(String customerFullName) {
        this.customerFullName = customerFullName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public CarCategory getCarCategory() {
        return carCategory;
    }

    public void setCarCategory(CarCategory carCategory) {
        this.carCategory = carCategory;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(String receiveDate) {
        this.receiveDate = receiveDate;
    }

    public String getPlannedReturnDate() {
        return plannedReturnDate;
    }

    public void setPlannedReturnDate(String plannedReturnDate) {
        this.plannedReturnDate = plannedReturnDate;
    }

    public String getReturnedDate() {
        return returnedDate;
    }

    public void setReturnedDate(String returnedDate) {
        this.returnedDate = returnedDate;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
