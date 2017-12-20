package hu.unideb.inf.carrental.user.resource.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import hu.unideb.inf.carrental.commons.domain.user.enumeration.UserRole;

/**
 * JsonProperty for supporting backward compatibility.
 */
public class UserResponse {
    @JsonProperty("userId")
    private Long id;

    @JsonProperty("userUsername")
    private String username;

    @JsonProperty("userEmail")
    private String email;

    @JsonProperty("userRole")
    private UserRole role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
