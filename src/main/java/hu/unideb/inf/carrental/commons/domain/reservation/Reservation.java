package hu.unideb.inf.carrental.commons.domain.reservation;

import hu.unideb.inf.carrental.commons.domain.car.Car;
import hu.unideb.inf.carrental.commons.domain.company.Company;
import hu.unideb.inf.carrental.commons.domain.customer.Customer;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
public class Reservation {
    private Long id;
    private Customer customer;
    private Company company;
    private Car car;
    private LocalDate receiveDate;
    private LocalDate plannedReturnDate;
    private LocalDate returnedDate;
    private Integer price;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @ManyToOne
    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @ManyToOne
    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    @NotNull
    public LocalDate getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(LocalDate receiveDate) {
        this.receiveDate = receiveDate;
    }

    @NotNull
    public LocalDate getPlannedReturnDate() {
        return plannedReturnDate;
    }

    public void setPlannedReturnDate(LocalDate plannedReturnDate) {
        this.plannedReturnDate = plannedReturnDate;
    }

    public LocalDate getReturnedDate() {
        return returnedDate;
    }

    public void setReturnedDate(LocalDate returnedDate) {
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

        Reservation that = (Reservation) o;

        if (customer != null ? !customer.equals(that.customer) : that.customer != null) return false;
        if (company != null ? !company.equals(that.company) : that.company != null) return false;
        if (car != null ? !car.equals(that.car) : that.car != null) return false;
        if (!receiveDate.equals(that.receiveDate)) return false;
        if (!plannedReturnDate.equals(that.plannedReturnDate)) return false;
        if (returnedDate != null ? !returnedDate.equals(that.returnedDate) : that.returnedDate != null) return false;
        return price.equals(that.price);
    }

    @Override
    public int hashCode() {
        int result = customer != null ? customer.hashCode() : 0;
        result = 31 * result + (company != null ? company.hashCode() : 0);
        result = 31 * result + (car != null ? car.hashCode() : 0);
        result = 31 * result + receiveDate.hashCode();
        result = 31 * result + plannedReturnDate.hashCode();
        result = 31 * result + (returnedDate != null ? returnedDate.hashCode() : 0);
        result = 31 * result + price.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", customerID=" + customer.getId() +
                ", companyID=" + company.getId() +
                ", carID=" + car.getId() +
                ", receiveDate=" + receiveDate +
                ", plannedReturnDate=" + plannedReturnDate +
                ", returnedDate=" + returnedDate +
                ", price=" + price +
                '}';
    }
}
