package hu.unideb.inf.carrental.reservation.resource.model;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

public class CreateReservationRequest {
    @NotNull
    private Long carId;

    @NotBlank
    private String receiveDate;

    @NotBlank
    private String plannedReturnDate;

    public CreateReservationRequest() {
    }

    public CreateReservationRequest(Long carId, String receiveDate, String plannedReturnDate) {
        this.carId = carId;
        this.receiveDate = receiveDate;
        this.plannedReturnDate = plannedReturnDate;
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CreateReservationRequest that = (CreateReservationRequest) o;

        if (!carId.equals(that.carId)) return false;
        if (!receiveDate.equals(that.receiveDate)) return false;
        return plannedReturnDate.equals(that.plannedReturnDate);
    }

    @Override
    public int hashCode() {
        int result = carId.hashCode();
        result = 31 * result + receiveDate.hashCode();
        result = 31 * result + plannedReturnDate.hashCode();
        return result;
    }
}
