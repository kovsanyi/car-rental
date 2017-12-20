package hu.unideb.inf.carrental.commons.exception;

public class UserAlreadyHasReservationException extends Exception {
    public UserAlreadyHasReservationException() {
        super("User already has a reservation");
    }
}
