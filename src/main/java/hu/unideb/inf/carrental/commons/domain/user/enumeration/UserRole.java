package hu.unideb.inf.carrental.commons.domain.user.enumeration;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
    ROLE_CUSTOMER,
    ROLE_COMPANY,
    ROLE_MANAGER;

    @Override
    public String getAuthority() {
        return toString();
    }

    public String getName() {
        return name();
    }
}
