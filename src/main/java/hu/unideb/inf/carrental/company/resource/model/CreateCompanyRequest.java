package hu.unideb.inf.carrental.company.resource.model;

import hu.unideb.inf.carrental.commons.constant.Constants;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class CreateCompanyRequest {
    @NotBlank
    @Length(min = Constants.User.USERNAME_MIN_LENGTH,
            max = Constants.User.USERNAME_MAX_LENGTH)
    private String userUsername;

    @NotBlank
    @Length(min = Constants.User.PASSWORD_MIN_LENGTH,
            max = Constants.User.PASSWORD_MAX_LENGTH)
    private String userPassword;

    @NotBlank
    @Email
    @Length(max = Constants.User.EMAIL_MAX_LENGTH)
    private String userEmail;

    @NotBlank
    @Length(min = Constants.User.COMPANY_NAME_MIN_LENGTH,
            max = Constants.User.COMPANY_NAME_MAX_LENGTH)
    private String name;

    @NotBlank
    @Email
    @Length(max = Constants.User.EMAIL_MAX_LENGTH)
    private String email;

    @NotBlank
    @Length(min = Constants.User.FULL_NAME_MIN_LENGTH,
            max = Constants.User.FULL_NAME_MAX_LENGTH)
    private String fullName;

    @NotBlank
    @Length(min = Constants.User.PHONE_MIN_LENGTH,
            max = Constants.User.PHONE_MAX_LENGTH)
    private String phoneNumber;

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

    public CreateCompanyRequest() {
    }

    public CreateCompanyRequest(String userUsername, String userPassword, String userEmail, String name, String email,
                                String fullName, String phoneNumber, Integer zipCode, String city, String address) {
        this.userUsername = userUsername;
        this.userPassword = userPassword;
        this.userEmail = userEmail;
        this.name = name;
        this.email = email;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.zipCode = zipCode;
        this.city = city;
        this.address = address;
    }

    public String getUserUsername() {
        return userUsername;
    }

    public void setUserUsername(String userUsername) {
        this.userUsername = userUsername;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CreateCompanyRequest that = (CreateCompanyRequest) o;

        if (!userUsername.equals(that.userUsername)) return false;
        if (!userPassword.equals(that.userPassword)) return false;
        if (!userEmail.equals(that.userEmail)) return false;
        if (!name.equals(that.name)) return false;
        if (!email.equals(that.email)) return false;
        if (!fullName.equals(that.fullName)) return false;
        if (!phoneNumber.equals(that.phoneNumber)) return false;
        if (!zipCode.equals(that.zipCode)) return false;
        if (!city.equals(that.city)) return false;
        return address.equals(that.address);
    }

    @Override
    public int hashCode() {
        int result = userUsername.hashCode();
        result = 31 * result + userPassword.hashCode();
        result = 31 * result + userEmail.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + fullName.hashCode();
        result = 31 * result + phoneNumber.hashCode();
        result = 31 * result + zipCode.hashCode();
        result = 31 * result + city.hashCode();
        result = 31 * result + address.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "CreateCompanyRequest{" +
                "userUsername='" + userUsername + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", zipCode=" + zipCode +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
