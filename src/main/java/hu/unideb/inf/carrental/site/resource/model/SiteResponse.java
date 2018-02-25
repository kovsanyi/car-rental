package hu.unideb.inf.carrental.site.resource.model;

public class SiteResponse {
    private Long id;
    private Long companyId;
    private Long managerId;
    private String email;
    private String phoneNumber;
    private Integer zipCode;
    private String city;
    private String address;
    private String openingHours;

    public SiteResponse() {
    }

    public SiteResponse(Long id, Long companyId, Long managerId, String email, String phoneNumber,
                        Integer zipCode, String city, String address, String openingHours) {
        this.id = id;
        this.companyId = companyId;
        this.managerId = managerId;
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

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
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

        SiteResponse that = (SiteResponse) o;

        if (!id.equals(that.id)) return false;
        if (!companyId.equals(that.companyId)) return false;
        if (managerId != null ? !managerId.equals(that.managerId) : that.managerId != null) return false;
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
        result = 31 * result + companyId.hashCode();
        result = 31 * result + (managerId != null ? managerId.hashCode() : 0);
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
        return "SiteResponse{" +
                "id=" + id +
                ", companyId=" + companyId +
                ", managerId=" + managerId +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", zipCode=" + zipCode +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", openingHours='" + openingHours + '\'' +
                '}';
    }
}
