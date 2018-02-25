package hu.unideb.inf.carrental.commons.exception.handler;

import hu.unideb.inf.carrental.commons.exception.*;
import hu.unideb.inf.carrental.commons.exception.enumeration.ExceptionType;
import hu.unideb.inf.carrental.commons.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionHandlerResource {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleNotFound(NotFoundException e) {
        return new ErrorResponse(ExceptionType.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(UsernameAlreadyInUseException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ResponseBody
    public ErrorResponse handleUsernameAlreadyInUse(UsernameAlreadyInUseException e) {
        return new ErrorResponse(ExceptionType.USERNAME_EXISTS, e.getMessage());
    }

    @ExceptionHandler(EmailAlreadyInUseException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ResponseBody
    public ErrorResponse handleEmailAlreadyInUse(EmailAlreadyInUseException e) {
        return new ErrorResponse(ExceptionType.EMAIL_EXISTS, e.getMessage());
    }

    @ExceptionHandler(NameAlreadyInUseException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ResponseBody
    public ErrorResponse handleNameAlreadyInUse(NameAlreadyInUseException e) {
        return new ErrorResponse(ExceptionType.NAME_EXISTS, e.getMessage());
    }

    @ExceptionHandler(CompanyEmailAlreadyInUseException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ResponseBody
    public ErrorResponse handleCompanyEmailAlreadyInUse(CompanyEmailAlreadyInUseException e) {
        return new ErrorResponse(ExceptionType.COMPANY_EMAIL_EXISTS, e.getMessage());
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorResponse handleUnauthorizedAccess(UnauthorizedAccessException e) {
        return new ErrorResponse(ExceptionType.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(CarInRentException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ErrorResponse handleCarAlreadyInRent(CarInRentException e) {
        return new ErrorResponse(ExceptionType.CAR_IN_RENT, e.getMessage());
    }

    @ExceptionHandler(ManagerCollisionException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ErrorResponse handleManagerCollision(ManagerCollisionException e) {
        return new ErrorResponse(ExceptionType.MANAGER_COLLISION, e.getMessage());
    }

    @ExceptionHandler(ReservationCollisionException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ErrorResponse handleReservationCollision(ReservationCollisionException e) {
        return new ErrorResponse(ExceptionType.RESERVATION_COLLISION, e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ErrorResponse handleIllegalArgument(IllegalArgumentException e) {
        return new ErrorResponse(ExceptionType.ILLEGAL_ARGUMENT, e.getMessage());
    }

    @ExceptionHandler(CollisionException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ErrorResponse handleCollision(CollisionException e) {
        return new ErrorResponse(ExceptionType.COLLISION, e.getMessage());
    }

    @ExceptionHandler(InvalidInputException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ErrorResponse handleInvalidInput(InvalidInputException e) {
        return new ErrorResponse(ExceptionType.ILLEGAL_ARGUMENT, e.getMessage());
    }
}
