package hu.unideb.inf.carrental.commons.domain.customer;

import hu.unideb.inf.carrental.commons.constant.Constants;
import hu.unideb.inf.carrental.commons.domain.user.User;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
public class Customer {
    private Long id;
    private User user;
    private String fullName;
    private String phoneNumber;
    private Integer zipCode;
    private String city;
    private String address;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @NotBlank
    @Length(min = Constants.User.FULL_NAME_MIN_LENGTH,
            max = Constants.User.FULL_NAME_MAX_LENGTH)
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @NotBlank
    @Length(min = Constants.User.PHONE_MIN_LENGTH,
            max = Constants.User.PHONE_MAX_LENGTH)
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @NotNull
    @Min(Constants.User.ZIP_CODE_MIN_VALUE)
    @Max(Constants.User.ZIP_CODE_MAX_VALUE)
    public Integer getZipCode() {
        return zipCode;
    }

    public void setZipCode(Integer zipCode) {
        this.zipCode = zipCode;
    }

    @NotBlank
    @Length(min = Constants.User.CITY_MIN_LENGTH,
            max = Constants.User.CITY_MAX_LENGTH)
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @NotBlank
    @Length(min = Constants.User.ADDRESS_MIN_LENGTH,
            max = Constants.User.ADDRESS_MAX_LENGTH)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        if (!user.getId().equals(customer.user.getId())) return false;
        if (!fullName.equals(customer.fullName)) return false;
        if (!phoneNumber.equals(customer.phoneNumber)) return false;
        if (!zipCode.equals(customer.zipCode)) return false;
        if (!city.equals(customer.city)) return false;
        return address.equals(customer.address);
    }

    @Override
    public int hashCode() {
        int result = user.getId().hashCode();
        result = 31 * result + fullName.hashCode();
        result = 31 * result + phoneNumber.hashCode();
        result = 31 * result + zipCode.hashCode();
        result = 31 * result + city.hashCode();
        result = 31 * result + address.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", userId=" + user.getId() +
                ", fullName='" + fullName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", zipCode=" + zipCode +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
