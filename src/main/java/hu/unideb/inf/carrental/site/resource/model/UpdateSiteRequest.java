package hu.unideb.inf.carrental.site.resource.model;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class UpdateSiteRequest {
    @NotNull
    private Long id;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String phoneNumber;

    @NotNull
    @Min(1000)
    @Max(10000)
    private Integer zipCode;

    @NotBlank
    private String city;

    @NotBlank
    private String address;

    @NotBlank
    private String openingHours;

    public UpdateSiteRequest() {
    }

    public UpdateSiteRequest(Long id, String email, String phoneNumber, Integer zipCode, String city, String address, String openingHours) {
        this.id = id;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.zipCode = zipCode;
        this.city = city;
        this.address = address;
        this.openingHours = openingHours;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }
}