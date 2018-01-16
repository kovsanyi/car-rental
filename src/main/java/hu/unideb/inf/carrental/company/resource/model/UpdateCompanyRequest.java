package hu.unideb.inf.carrental.company.resource.model;

import hu.unideb.inf.carrental.commons.constant.Constants;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class UpdateCompanyRequest {
    @NotBlank
    @Length(min = Constants.User.COMPANY_NAME_MIN_LENGTH,
            max = Constants.User.COMPANY_NAME_MAX_LENGTH)
    private String name;

    @NotBlank
    @Email
    @Length(max = Constants.User.EMAIL_MAX_LENGTH)
    private String email;

    @NotBlank
    @Length(min = Constants.User.PHONE_MIN_LENGTH,
            max = Constants.User.PHONE_MAX_LENGTH)
    private String phoneNumber;

    @NotBlank
    @Length(min = Constants.User.FULL_NAME_MIN_LENGTH,
            max = Constants.User.FULL_NAME_MAX_LENGTH)
    private String fullName;

    @NotNull
    @Min(Constants.User.ZIP_CODE_MIN_VALUE)
    @Max(Constants.User.ZIP_CODE_MAX_VALUE)
    private Integer zipCode;

    @NotBlank
    @Length(min = Constants.User.CITY_MIN_LENGTH,
            max = Constants.User.CITY_MAX_LENGTH)
    private String city;

    @NotBlank
    @Length(min = Constants.User.ADDRESS_MIN_LENGTH,
            max = Constants.User.ADDRESS_MAX_LENGTH)
    private String address;

    public UpdateCompanyRequest() {
    }

    public UpdateCompanyRequest(String name, String email, String phoneNumber, String fullName,
                                Integer zipCode, String city, String address) {
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
