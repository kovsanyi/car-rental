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
}
