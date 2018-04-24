package hu.unideb.inf.carrental.commons.domain.site;

import hu.unideb.inf.carrental.commons.constant.Constants;
import hu.unideb.inf.carrental.commons.domain.company.Company;
import hu.unideb.inf.carrental.commons.domain.manager.Manager;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
public class Site {
    private Long id;
    private Company company;
    private Manager manager;
    private String email;
    private String phoneNumber;
    private Integer zipCode;
    private String city;
    private String address;
    private String openingHours;

    public Site() {
    }

    public Site(Long id, Company company, Manager manager, String email, String phoneNumber, Integer zipCode,
                String city, String address, String openingHours) {
        this.id = id;
        this.company = company;
        this.manager = manager;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.zipCode = zipCode;
        this.city = city;
        this.address = address;
        this.openingHours = openingHours;
    }

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @OneToOne
    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    @NotBlank
    @Email
    @Length(max = Constants.Site.EMAIL_MAX_LENGTH)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NotBlank
    @Length(min = Constants.Site.PHONE_MIN_LENGTH,
            max = Constants.Site.PHONE_MAX_LENGTH)
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @NotNull
    @Min(Constants.Site.ZIP_CODE_MIN_VALUE)
    @Max(Constants.Site.ZIP_CODE_MAX_VALUE)
    public Integer getZipCode() {
        return zipCode;
    }

    public void setZipCode(Integer zipCode) {
        this.zipCode = zipCode;
    }

    @NotBlank
    @Length(min = Constants.Site.CITY_MIN_LENGTH,
            max = Constants.Site.CITY_MAX_LENGTH)
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @NotBlank
    @Length(min = Constants.Site.ADDRESS_MIN_LENGTH,
            max = Constants.Site.ADDRESS_MAX_LENGTH)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @NotBlank
    @Length(min = Constants.Site.OPENING_HOURS_MIN_LENGTH,
            max = Constants.Site.OPENING_HOURS_MAX_LENGTH)
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

        Site site = (Site) o;

        if (!id.equals(site.id)) return false;
        if (!company.getId().equals(site.company.getId())) return false;
        if (!manager.equals(site.manager)) return false;
        if (!email.equals(site.email)) return false;
        if (!phoneNumber.equals(site.phoneNumber)) return false;
        if (!zipCode.equals(site.zipCode)) return false;
        if (!city.equals(site.city)) return false;
        if (!address.equals(site.address)) return false;
        return openingHours.equals(site.openingHours);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + company.getId().hashCode();
        result = 31 * result + manager.hashCode();
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
        return "Site{" +
                "id=" + id +
                ", companyId=" + company.getId() +
                ", manager=" + manager +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", zipCode=" + zipCode +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", openingHours='" + openingHours + '\'' +
                '}';
    }
}
