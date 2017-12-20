package hu.unideb.inf.carrental.reservation.resource;

import hu.unideb.inf.carrental.commons.exception.CarInRentException;
import hu.unideb.inf.carrental.commons.exception.NotFoundException;
import hu.unideb.inf.carrental.commons.exception.ReservationCollisionException;
import hu.unideb.inf.carrental.commons.exception.UnauthorizedAccessException;
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

    @PostMapping("/reserve")
    public ResponseEntity reserve(@RequestBody CreateReservationRequest createReservationRequest)
            throws NotFoundException, CarInRentException, ReservationCollisionException {
        return new ResponseEntity<>(new CreatedResponse(reservationService.reserve(createReservationRequest)), HttpStatus.CREATED);
    }

    @PutMapping("/close/{reservationId}")
    public ResponseEntity close(@PathVariable("reservationId") long reservationId) throws NotFoundException, UnauthorizedAccessException {
        reservationService.close(reservationId);
        return new ResponseEntity<>(new SuccessResponse(), HttpStatus.ACCEPTED);
    }

    @GetMapping("/customer/active")
    public ResponseEntity<?> getActiveByCustomer() throws NotFoundException {
        return new ResponseEntity<>(reservationService.getActiveByCustomer(), HttpStatus.OK);
    }

    @GetMapping("/customer/closed")
    public ResponseEntity<?> getClosedByCustomer() {
        return new ResponseEntity<>(reservationService.getClosedByCustomer(), HttpStatus.OK);
    }

    @GetMapping("/customer/all")
    public ResponseEntity<?> getAllByCustomer() {
        return new ResponseEntity<>(reservationService.getAllByCustomer(), HttpStatus.OK);
    }

    @GetMapping("/siteId/{siteId}/active")
    public ResponseEntity getActiveBySite(@PathVariable("siteId") long siteId) throws NotFoundException, UnauthorizedAccessException {
        return new ResponseEntity<>(reservationService.getActiveBySiteId(siteId), HttpStatus.OK);
    }

    @GetMapping("/siteId/{siteId}/closed")
    public ResponseEntity getClosedBySite(@PathVariable("siteId") long siteId) throws NotFoundException, UnauthorizedAccessException {
        return new ResponseEntity<>(reservationService.getClosedBySiteId(siteId), HttpStatus.OK);
    }

    @GetMapping("/siteId/{siteId}/all")
    public ResponseEntity getAllBySite(@PathVariable("siteId") long siteId) throws NotFoundException, UnauthorizedAccessException {
        return new ResponseEntity<>(reservationService.getAllBySiteId(siteId), HttpStatus.OK);
    }

    @GetMapping("/company/active")
    public ResponseEntity getActiveByCompany() {
        return new ResponseEntity<>(reservationService.getActiveByCompany(), HttpStatus.OK);
    }

    @GetMapping("/company/closed")
    public ResponseEntity getClosedByCompany() {
        return new ResponseEntity<>(reservationService.getClosedByCompany(), HttpStatus.OK);
    }

    @GetMapping("/company/all")
    public ResponseEntity getAllByCompany() {
        return new ResponseEntity<>(reservationService.getAllByCompany(), HttpStatus.OK);
    }
}
