package hu.unideb.inf.carrental.site.resource.model;

import hu.unideb.inf.carrental.commons.constant.Constants;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class UpdateSiteRequest {
    @NotNull
    private Long id;

    @NotBlank
    @Email
    @Length(max = Constants.Site.EMAIL_MAX_LENGTH)
    private String email;

    @NotBlank
    @Length(min = Constants.Site.PHONE_MIN_LENGTH,
            max = Constants.Site.PHONE_MAX_LENGTH)
    private String phoneNumber;

    @NotNull
    @Min(Constants.Site.ZIP_CODE_MIN_VALUE)
    @Max(Constants.Site.ZIP_CODE_MAX_VALUE)
    private Integer zipCode;

    @NotBlank
    @Length(min = Constants.Site.CITY_MIN_LENGTH,
            max = Constants.Site.CITY_MAX_LENGTH)
    private String city;

    @NotBlank
    @Length(min = Constants.Site.ADDRESS_MIN_LENGTH,
            max = Constants.Site.ADDRESS_MAX_LENGTH)
    private String address;

    @NotBlank
    @Length(min = Constants.Site.OPENING_HOURS_MIN_LENGTH,
            max = Constants.Site.OPENING_HOURS_MAX_LENGTH)
    private String openingHours;

    public UpdateSiteRequest() {
    }

    public UpdateSiteRequest(Long id, String email, String phoneNumber, Integer zipCode,
                             String city, String address, String openingHours) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UpdateSiteRequest that = (UpdateSiteRequest) o;

        if (!id.equals(that.id)) return false;
        if (!email.equals(that.email)) return false;
        if (!phoneNumber.equals(that.phoneNumber)) return false;
        if (!zipCode.equals(that.zipCode)) return false;
        if (!city.equals(that.city)) return false;
        if (!address.equals(that.address)) return false;
        return openingHours.equals(that.openingHours);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + phoneNumber.hashCode();
        result = 31 * result + zipCode.hashCode();
        result = 31 * result + city.hashCode();
        result = 31 * result + address.hashCode();
        result = 31 * result + openingHours.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "UpdateSiteRequest{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", zipCode=" + zipCode +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", openingHours='" + openingHours + '\'' +
                '}';
    }
}
