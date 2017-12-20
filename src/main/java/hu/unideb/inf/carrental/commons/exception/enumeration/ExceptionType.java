package hu.unideb.inf.carrental.commons.exception.enumeration;

public enum ExceptionType {
    NOT_FOUND("not found"),
    UNAUTHORIZED("unauthorized"),
    BAD_REQUEST("bad request"),
    USERNAME_EXISTS("username"),
    EMAIL_EXISTS("email"),
    NAME_EXISTS("name"),
    COMPANY_EMAIL_EXISTS("company email"),
    CAR_IN_RENT("car in rent"),
    COLLISION("collision"),
    MANAGER_COLLISION("manager collision"),
    RESERVATION_COLLISION("reservation collision"),
    ILLEGAL_ARGUMENT("illegal argument");

    private final String text;

    ExceptionType(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
