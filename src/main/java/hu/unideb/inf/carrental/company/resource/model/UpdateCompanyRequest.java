package hu.unideb.inf.carrental.company.resource.model;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class UpdateCompanyRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String email;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String fullName;

    @NotNull
    @Min(1000)
    @Max(10000)
    private Integer zipCode;

    @NotBlank
    private String city;

    @NotBlank
    private String address;

    public UpdateCompanyRequest() {
    }

    public UpdateCompanyRequest(String name, String email, String phoneNumber, String fullName, Integer zipCode, String city, String address) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.fullName = fullName;
        this.zipCode = zipCode;
        this.city = city;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getZipCode() {
        return zipCode;
    }

    public void setZipCode(Integer zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
