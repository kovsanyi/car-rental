package hu.unideb.inf.carrental.commons.exception;

public class OwnerCollisionException extends Exception {
    public OwnerCollisionException() {
        super("User already has a company");
    }
}
