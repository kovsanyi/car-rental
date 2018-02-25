package hu.unideb.inf.carrental.reservation.resource;

import hu.unideb.inf.carrental.commons.exception.*;
import hu.unideb.inf.carrental.commons.model.CreatedResponse;
import hu.unideb.inf.carrental.commons.model.SuccessResponse;
import hu.unideb.inf.carrental.reservation.resource.model.CreateReservationRequest;
import hu.unideb.inf.carrental.reservation.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservation")
public class ReservationResource {
    private final ReservationService reservationService;

    @Autowired
    public ReservationResource(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping(RESERVE)
    public ResponseEntity reserve(@RequestBody CreateReservationRequest createReservationRequest)
            throws NotFoundException, CarInRentException, ReservationCollisionException, InvalidInputException {
        return new ResponseEntity<>(
                new CreatedResponse(reservationService.reserve(createReservationRequest)), HttpStatus.CREATED);
    }

    @PutMapping(CLOSE)
    public ResponseEntity close(@PathVariable("reservationId") long reservationId)
            throws NotFoundException, UnauthorizedAccessException {
        reservationService.close(reservationId);
        return new ResponseEntity<>(new SuccessResponse(), HttpStatus.ACCEPTED);
    }

    @GetMapping(ACTIVE_BY_CUSTOMER)
    public ResponseEntity<?> getActiveByCustomer() throws NotFoundException {
        return new ResponseEntity<>(reservationService.getActiveByCustomer(), HttpStatus.OK);
    }

    @GetMapping(CLOSED_BY_CUSTOMER)
    public ResponseEntity<?> getClosedByCustomer() {
        return new ResponseEntity<>(reservationService.getClosedByCustomer(), HttpStatus.OK);
    }

    @GetMapping(ALL_BY_CUSTOMER)
    public ResponseEntity<?> getAllByCustomer() {
        return new ResponseEntity<>(reservationService.getAllByCustomer(), HttpStatus.OK);
    }

    @GetMapping(ACTIVE_BY_SITE)
    public ResponseEntity getActiveBySite(@PathVariable("siteId") long siteId)
            throws NotFoundException, UnauthorizedAccessException {
        return new ResponseEntity<>(reservationService.getActiveBySiteId(siteId), HttpStatus.OK);
    }

    @GetMapping(CLOSED_BY_SITE)
    public ResponseEntity getClosedBySite(@PathVariable("siteId") long siteId)
            throws NotFoundException, UnauthorizedAccessException {
        return new ResponseEntity<>(reservationService.getClosedBySiteId(siteId), HttpStatus.OK);
    }

    @GetMapping(ALL_BY_SITE)
    public ResponseEntity getAllBySite(@PathVariable("siteId") long siteId)
            throws NotFoundException, UnauthorizedAccessException {
        return new ResponseEntity<>(reservationService.getAllBySiteId(siteId), HttpStatus.OK);
    }

    @GetMapping(ACTIVE_BY_COMPANY)
    public ResponseEntity getActiveByCompany() {
        return new ResponseEntity<>(reservationService.getActiveByCompany(), HttpStatus.OK);
    }

    @GetMapping(CLOSED_BY_COMPANY)
    public ResponseEntity getClosedByCompany() {
        return new ResponseEntity<>(reservationService.getClosedByCompany(), HttpStatus.OK);
    }

    @GetMapping(ALL_BY_COMPANY)
    public ResponseEntity getAllByCompany() {
        return new ResponseEntity<>(reservationService.getAllByCompany(), HttpStatus.OK);
    }

    public static final String RESERVE = "/reserve";
    public static final String CLOSE = "/close/{reservationId}";
    public static final String ACTIVE_BY_CUSTOMER = "/customer/active";
    public static final String CLOSED_BY_CUSTOMER = "/customer/closed";
    public static final String ALL_BY_CUSTOMER = "/customer/all";
    public static final String ACTIVE_BY_SITE = "/siteId/{siteId}/active";
    public static final String CLOSED_BY_SITE = "/siteId/{siteId}/closed";
    public static final String ALL_BY_SITE = "/siteId/{siteId}/all";
    public static final String ACTIVE_BY_COMPANY = "/company/active";
    public static final String CLOSED_BY_COMPANY = "/company/closed";
    public static final String ALL_BY_COMPANY = "/company/all";
}
