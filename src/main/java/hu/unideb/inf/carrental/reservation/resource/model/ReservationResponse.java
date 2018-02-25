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

    public ReservationResponse(Long id, String customerUserUsername, String customerFullName, String companyName,
                               Long carId, CarCategory carCategory, String carBrand, String carModel, String receiveDate,
                               String plannedReturnDate, String returnedDate, Integer price) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReservationResponse that = (ReservationResponse) o;

        if (!id.equals(that.id)) return false;
        if (customerUserUsername != null ? !customerUserUsername.equals(that.customerUserUsername) : that.customerUserUsername != null)
            return false;
        if (customerFullName != null ? !customerFullName.equals(that.customerFullName) : that.customerFullName != null)
            return false;
        if (companyName != null ? !companyName.equals(that.companyName) : that.companyName != null) return false;
        if (carId != null ? !carId.equals(that.carId) : that.carId != null) return false;
        if (carCategory != that.carCategory) return false;
        if (carBrand != null ? !carBrand.equals(that.carBrand) : that.carBrand != null) return false;
        if (carModel != null ? !carModel.equals(that.carModel) : that.carModel != null) return false;
        if (!receiveDate.equals(that.receiveDate)) return false;
        if (!plannedReturnDate.equals(that.plannedReturnDate)) return false;
        if (returnedDate != null ? !returnedDate.equals(that.returnedDate) : that.returnedDate != null) return false;
        return price.equals(that.price);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (customerUserUsername != null ? customerUserUsername.hashCode() : 0);
        result = 31 * result + (customerFullName != null ? customerFullName.hashCode() : 0);
        result = 31 * result + (companyName != null ? companyName.hashCode() : 0);
        result = 31 * result + (carId != null ? carId.hashCode() : 0);
        result = 31 * result + (carCategory != null ? carCategory.hashCode() : 0);
        result = 31 * result + (carBrand != null ? carBrand.hashCode() : 0);
        result = 31 * result + (carModel != null ? carModel.hashCode() : 0);
        result = 31 * result + receiveDate.hashCode();
        result = 31 * result + plannedReturnDate.hashCode();
        result = 31 * result + (returnedDate != null ? returnedDate.hashCode() : 0);
        result = 31 * result + price.hashCode();
        return result;
    }
}
