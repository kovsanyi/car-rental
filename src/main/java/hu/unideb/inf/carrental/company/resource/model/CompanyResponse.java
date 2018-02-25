package hu.unideb.inf.carrental.company.resource.model;

public class CompanyResponse {
    private Long id;
    private Long userId;
    private String userUsername;
    private String userEmail;
    private String userRole;
    private String name;
    private String email;
    private String fullName;
    private String phoneNumber;
    private Integer zipCode;
    private String city;
    private String address;

    public CompanyResponse() {
    }

    public CompanyResponse(Long id, Long userId, String userUsername, String userEmail, String userRole, String name,
                           String email, String fullName, String phoneNumber, Integer zipCode, String city, String address) {
        this.id = id;
        this.userId = userId;
        this.userUsername = userUsername;
        this.userEmail = userEmail;
        this.userRole = userRole;
        this.name = name;
        this.email = email;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.zipCode = zipCode;
        this.city = city;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserUsername() {
        return userUsername;
    }

    public void setUserUsername(String userUsername) {
        this.userUsername = userUsername;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompanyResponse that = (CompanyResponse) o;

        if (!id.equals(that.id)) return false;
        if (!userId.equals(that.userId)) return false;
        if (!userUsername.equals(that.userUsername)) return false;
        if (!userEmail.equals(that.userEmail)) return false;
        if (!userRole.equals(that.userRole)) return false;
        if (!name.equals(that.name)) return false;
        if (!email.equals(that.email)) return false;
        if (!phoneNumber.equals(that.phoneNumber)) return false;
        if (!fullName.equals(that.fullName)) return false;
        if (!zipCode.equals(that.zipCode)) return false;
        if (!city.equals(that.city)) return false;
        return address.equals(that.address);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + userId.hashCode();
        result = 31 * result + userUsername.hashCode();
        result = 31 * result + userEmail.hashCode();
        result = 31 * result + userRole.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + phoneNumber.hashCode();
        result = 31 * result + fullName.hashCode();
        result = 31 * result + zipCode.hashCode();
        result = 31 * result + city.hashCode();
        result = 31 * result + address.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "CompanyResponse{" +
                "id=" + id +
                ", userId=" + userId +
                ", userUsername='" + userUsername + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userRole='" + userRole + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", fullName='" + fullName + '\'' +
                ", zipCode=" + zipCode +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
