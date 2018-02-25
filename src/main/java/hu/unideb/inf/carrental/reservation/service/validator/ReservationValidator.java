package hu.unideb.inf.carrental.reservation.service.validator;

import hu.unideb.inf.carrental.commons.constant.Constants;
import hu.unideb.inf.carrental.commons.domain.customer.CustomerRepository;
import hu.unideb.inf.carrental.commons.domain.reservation.Reservation;
import hu.unideb.inf.carrental.commons.domain.reservation.ReservationRepository;
import hu.unideb.inf.carrental.commons.exception.CarInRentException;
import hu.unideb.inf.carrental.commons.exception.InvalidInputException;
import hu.unideb.inf.carrental.commons.exception.ReservationCollisionException;
import hu.unideb.inf.carrental.commons.exception.UnauthorizedAccessException;
import hu.unideb.inf.carrental.commons.security.SecurityUtils;
import hu.unideb.inf.carrental.site.service.validator.SiteValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class ReservationValidator {
    private static final Logger logger = LoggerFactory.getLogger(ReservationValidator.class);

    private final ReservationRepository reservationRepository;
    private final CustomerRepository customerRepository;
    private final SiteValidator siteValidator;

    @Autowired
    public ReservationValidator(ReservationRepository reservationRepository, CustomerRepository customerRepository,
                                SiteValidator siteValidator) {
        this.reservationRepository = reservationRepository;
        this.customerRepository = customerRepository;
        this.siteValidator = siteValidator;
    }

    public void validate(Reservation reservation)
            throws CarInRentException, ReservationCollisionException, InvalidInputException {
        logger.info("Validating reservation");
        if (reservationRepository.findByCustomerAndReturnedDateIsNull(
                customerRepository.findByUser(SecurityUtils.getLoggedInUser()).get()).isPresent()) {
            throw new ReservationCollisionException(Constants.CUSTOMER_HAS_RESERVATION);
        }
        if (reservation.getReceiveDate().isBefore(LocalDate.now())) {
            throw new InvalidInputException(Constants.INVALID_DATE);
        }
        if (reservation.getReceiveDate().isAfter(reservation.getPlannedReturnDate())) {
            throw new InvalidInputException(Constants.INVALID_DATE);
        }
        Optional<Reservation> previousReservation = reservationRepository.findByCarAndReturnedDateIsNull(reservation.getCar());
        if (previousReservation.isPresent()) {
            LocalDate receive = reservation.getReceiveDate();
            LocalDate plannedReturn = reservation.getPlannedReturnDate();
            LocalDate prevReceive = previousReservation.get().getReceiveDate();
            LocalDate prevPlannedReturn = previousReservation.get().getPlannedReturnDate();
            if ((receive.isAfter(prevReceive) || (receive.isEqual(prevReceive)))
                    && (receive.isBefore(prevPlannedReturn) || receive.isEqual(prevPlannedReturn))) {
                throw new CarInRentException(Constants.CAR_ALREADY_IN_RENT);
            }
            if ((plannedReturn.isAfter(prevReceive) || plannedReturn.isEqual(prevReceive))
                    && (plannedReturn.isBefore(prevPlannedReturn) || plannedReturn.isEqual(prevPlannedReturn))) {
                throw new CarInRentException(Constants.CAR_ALREADY_IN_RENT);
            }
        }
    }

    public void validateClosing(Reservation reservation) throws UnauthorizedAccessException {
        logger.info("Validating reservation to be closed");
        siteValidator.validate(reservation.getCar().getSite());
    }
}
