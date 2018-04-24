package hu.unideb.inf.carrental.reservation.service;

import hu.unideb.inf.carrental.commons.constant.Constants;
import hu.unideb.inf.carrental.commons.domain.car.CarRepository;
import hu.unideb.inf.carrental.commons.domain.company.Company;
import hu.unideb.inf.carrental.commons.domain.company.CompanyRepository;
import hu.unideb.inf.carrental.commons.domain.customer.Customer;
import hu.unideb.inf.carrental.commons.domain.customer.CustomerRepository;
import hu.unideb.inf.carrental.commons.domain.reservation.Reservation;
import hu.unideb.inf.carrental.commons.domain.reservation.ReservationRepository;
import hu.unideb.inf.carrental.commons.domain.site.Site;
import hu.unideb.inf.carrental.commons.domain.site.SiteRepository;
import hu.unideb.inf.carrental.commons.exception.*;
import hu.unideb.inf.carrental.commons.security.SecurityUtils;
import hu.unideb.inf.carrental.reservation.resource.model.CreateReservationRequest;
import hu.unideb.inf.carrental.reservation.resource.model.ReservationResponse;
import hu.unideb.inf.carrental.reservation.service.converter.CreateReservationRequestConverter;
import hu.unideb.inf.carrental.reservation.service.converter.ReservationResponseConverter;
import hu.unideb.inf.carrental.reservation.service.validator.ReservationValidator;
import hu.unideb.inf.carrental.site.service.validator.SiteValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for creating, managing and closing the reservations.
 *
 * @see CreateReservationRequest
 * @see ReservationResponse
 */
@Service
public class ReservationService {
    /**
     * Reserves the selected car for the specified period for the logged in customer.
     * <b>This method can only be called by a customer.</b>
     *
     * @param createReservationRequest a request object to create a new reservation
     * @return the ID of the reservation
     * @throws NotFoundException             if the car ID is invalid
     * @throws CarInRentException            if the selected car can not be rented for the selected period
     * @throws ReservationCollisionException if the customer has an active reservation
     * @throws InvalidInputException         if the specified period is invalid
     * @see hu.unideb.inf.carrental.customer.service.CustomerService
     */
    @Secured("ROLE_CUSTOMER")
    @Transactional
    public long reserve(CreateReservationRequest createReservationRequest)
            throws NotFoundException, CarInRentException, ReservationCollisionException, InvalidInputException {
        LOGGER.info("Creating reservation");
        Reservation reservation = createReservationRequestConverter.from(createReservationRequest);
        reservation.setCustomer(getCustomer());
        reservation.setCar(carRepository.findById(reservation.getCar().getId())
                .orElseThrow(() -> new NotFoundException(Constants.CAR_NOT_FOUND)));
        reservation.setCompany(reservation.getCar().getSite().getCompany());
        reservationValidator.validate(reservation);

        reservation.setPrice(getPrice(reservation.getReceiveDate(), reservation.getPlannedReturnDate(),
                reservation.getCar().getPrice()));
        return reservationRepository.save(reservation).getId();
    }

    /**
     * Closes the active reservation by reservation ID.<br>
     * <b>This method can only be called by a company owner or a manager who has rights to modify site where the
     * car found.</b>
     *
     * @param id ID of the reservation
     * @throws NotFoundException           if the reservation ID is invalid
     * @throws UnauthorizedAccessException if the logged in user has no right to close the reservation
     * @see hu.unideb.inf.carrental.site.service.SiteService
     */
    @Secured({"ROLE_COMPANY", "ROLE_MANAGER"})
    public void close(long id) throws NotFoundException, UnauthorizedAccessException {
        LOGGER.info("Closing reservation ID {}", id);
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.RESERVATION_NOT_FOUND));
        reservationValidator.validateClosing(reservation);

        reservation.setReturnedDate(LocalDate.now());
        reservation.setPrice(getPrice(reservation.getReceiveDate(), LocalDate.now(), reservation.getCar().getPrice()));
        reservationRepository.save(reservation);
    }

    /**
     * Returns the active reservations by the logged in customer.<br>
     * <b>This method can only be called by a customer.</b>
     *
     * @return the active reservations by the logged in customer
     * @throws NotFoundException if the customer has not got an active reservation
     * @see hu.unideb.inf.carrental.customer.service.CustomerService
     */
    @Secured("ROLE_CUSTOMER")
    @Transactional
    public ReservationResponse getActiveByCustomer() throws NotFoundException {
        LOGGER.info("Providing active reservation to customer");
        return reservationResponseConverter.from(reservationRepository.findByCustomerAndReturnedDateIsNull(getCustomer())
                .orElseThrow(() -> new NotFoundException(Constants.NO_ACTIVE_RESERVATION)));
    }

    /**
     * Returns the closed reservations by the logged in customer.<br>
     * <b>This method can only be called by a customer.</b>
     *
     * @return the closed reservations by the logged in customer
     * @see hu.unideb.inf.carrental.customer.service.CustomerService
     */
    @Secured("ROLE_CUSTOMER")
    @Transactional
    public List<ReservationResponse> getClosedByCustomer() {
        LOGGER.info("Providing all closed reservation to customer");
        return reservationRepository.findByCustomerAndReturnedDateIsNotNull(getCustomer()).stream()
                .map(reservationResponseConverter::from).collect(Collectors.toList());
    }

    /**
     * Returns all the reservations including active and closed ones by the logged in customer.<br>
     * <b>This method can only be called by a customer.</b>
     *
     * @return all the reservations including active and closed ones by the logged in customer
     * @see hu.unideb.inf.carrental.customer.service.CustomerService
     */
    @Secured("ROLE_CUSTOMER")
    @Transactional
    public List<ReservationResponse> getAllByCustomer() {
        LOGGER.info("Providing all reservation to customer");
        return reservationRepository.findByCustomer(getCustomer()).stream()
                .map(reservationResponseConverter::from).collect(Collectors.toList());
    }

    /**
     * Returns the active reservations by site ID.<br>
     * <b>This method can only be called by a company owner or a manager who has rights to modify site where the
     * car found.</b>
     *
     * @param siteId ID of the site
     * @return the active reservations by site ID
     * @throws NotFoundException           if the site ID is invalid
     * @throws UnauthorizedAccessException if the logged in user has no right to get the specified reservation
     * @see hu.unideb.inf.carrental.site.service.SiteService
     */
    @Secured({"ROLE_COMPANY", "ROLE_MANAGER"})
    @Transactional
    public List<ReservationResponse> getActiveBySiteId(long siteId) throws NotFoundException, UnauthorizedAccessException {
        LOGGER.info("Providing active reservations of site ID {}", siteId);
        siteValidator.validate(siteId);
        return reservationRepository.findByCarSiteAndReturnedDateIsNull(getSite(siteId)).stream()
                .map(reservationResponseConverter::from).collect(Collectors.toList());
    }

    /**
     * Returns the closed reservations by site ID.<br>
     * <b>This method can only be called by a company owner or a manager who has rights to modify site where the
     * car found.</b>
     *
     * @param siteId ID of the site
     * @return the closed reservations by site ID
     * @throws NotFoundException           if the site ID is invalid
     * @throws UnauthorizedAccessException if the logged in user has no right to get the specified reservation
     * @see hu.unideb.inf.carrental.site.service.SiteService
     */
    @Secured({"ROLE_COMPANY", "ROLE_MANAGER"})
    @Transactional
    public List<ReservationResponse> getClosedBySiteId(long siteId) throws NotFoundException, UnauthorizedAccessException {
        LOGGER.info("Providing closed reservations of site ID {}", siteId);
        return reservationRepository.findByCarSiteAndReturnedDateIsNotNull(getSite(siteId)).stream()
                .map(reservationResponseConverter::from).collect(Collectors.toList());
    }

    /**
     * Returns all the reservations including active and closed ones by site ID.<br>
     * <b>This method can only be called by a company owner or a manager who has rights to modify site where the
     * car found.</b>
     *
     * @param siteId ID of the site
     * @return all the reservations including active and closed ones by site ID
     * @throws NotFoundException           if the site ID is invalid
     * @throws UnauthorizedAccessException if the logged in user has no right to get the specified reservation
     * @see hu.unideb.inf.carrental.site.service.SiteService
     */
    @Secured({"ROLE_COMPANY", "ROLE_MANAGER"})
    @Transactional
    public List<ReservationResponse> getAllBySiteId(long siteId) throws NotFoundException, UnauthorizedAccessException {
        LOGGER.info("Providing all reservation of site ID {}", siteId);
        return reservationRepository.findByCarSite(getSite(siteId)).stream()
                .map(reservationResponseConverter::from).collect(Collectors.toList());
    }

    /**
     * Returns the active reservations by the logged in company owner.<br>
     * <b>This method can only be called by a company owner.</b>
     *
     * @return the active reservations by the logged in company owner
     * @see hu.unideb.inf.carrental.company.service.CompanyService
     */
    @Secured("ROLE_COMPANY")
    @Transactional
    public List<ReservationResponse> getActiveByCompany() {
        LOGGER.info("Providing active reservations of company");
        return reservationRepository.findByCompanyAndReturnedDateIsNull(getCompany()).stream()
                .map(reservationResponseConverter::from).collect(Collectors.toList());
    }

    /**
     * Returns the closed reservations by the logged in company owner.<br>
     * <b>This method can only be called by a company owner.</b>
     *
     * @return the closed reservations by the logged in company owner
     * @see hu.unideb.inf.carrental.company.service.CompanyService
     */
    @Secured("ROLE_COMPANY")
    @Transactional
    public List<ReservationResponse> getClosedByCompany() {
        LOGGER.info("Providing closed reservations of company");
        return reservationRepository.findByCompanyAndReturnedDateIsNotNull(getCompany()).stream()
                .map(reservationResponseConverter::from).collect(Collectors.toList());
    }

    /**
     * Returns all the reservations including active and closed ones by the logged in company owner.<br>
     * <b>This method can only be called by a company owner.</b>
     *
     * @return all the reservations including active and closed ones by the logged in company owner
     * @see hu.unideb.inf.carrental.company.service.CompanyService
     */
    @Secured("ROLE_COMPANY")
    @Transactional
    public List<ReservationResponse> getAllByCompany() {
        LOGGER.info("Providing all reservation of company");
        return reservationRepository.findByCompany(getCompany()).stream()
                .map(reservationResponseConverter::from).collect(Collectors.toList());
    }

    private Customer getCustomer() {
        return customerRepository.findByUser(SecurityUtils.getLoggedInUser()).get();
    }

    private Company getCompany() {
        return companyRepository.findByUser(SecurityUtils.getLoggedInUser()).get();
    }

    private Site getSite(long siteId) throws NotFoundException, UnauthorizedAccessException {
        Site site = siteRepository.findById(siteId).orElseThrow(() -> new NotFoundException(Constants.SITE_NOT_FOUND));
        siteValidator.validate(site);
        return site;
    }

    private int getPrice(LocalDate first, LocalDate last, int carPrice) {
        int days = (int) ChronoUnit.DAYS.between(first, last);
        return (days + 1) * carPrice;
    }

    @Autowired
    public ReservationService(ReservationValidator reservationValidator, SiteValidator siteValidator,
                              ReservationRepository reservationRepository, CarRepository carRepository,
                              CompanyRepository companyRepository, SiteRepository siteRepository,
                              CustomerRepository customerRepository,
                              CreateReservationRequestConverter createReservationRequestConverter,
                              ReservationResponseConverter reservationResponseConverter) {
        this.reservationValidator = reservationValidator;
        this.siteValidator = siteValidator;
        this.reservationRepository = reservationRepository;
        this.carRepository = carRepository;
        this.companyRepository = companyRepository;
        this.siteRepository = siteRepository;
        this.customerRepository = customerRepository;
        this.createReservationRequestConverter = createReservationRequestConverter;
        this.reservationResponseConverter = reservationResponseConverter;
    }

    private final ReservationValidator reservationValidator;
    private final SiteValidator siteValidator;
    private final ReservationRepository reservationRepository;
    private final CarRepository carRepository;
    private final CompanyRepository companyRepository;
    private final SiteRepository siteRepository;
    private final CustomerRepository customerRepository;

    private final CreateReservationRequestConverter createReservationRequestConverter;
    private final ReservationResponseConverter reservationResponseConverter;

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationService.class);
}
