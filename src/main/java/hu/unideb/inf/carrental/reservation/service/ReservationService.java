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
import hu.unideb.inf.carrental.commons.exception.CarInRentException;
import hu.unideb.inf.carrental.commons.exception.NotFoundException;
import hu.unideb.inf.carrental.commons.exception.ReservationCollisionException;
import hu.unideb.inf.carrental.commons.exception.UnauthorizedAccessException;
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

@Service
public class ReservationService {
    private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);

    private final ReservationValidator reservationValidator;
    private final SiteValidator siteValidator;
    private final ReservationRepository reservationRepository;
    private final CarRepository carRepository;
    private final CompanyRepository companyRepository;
    private final SiteRepository siteRepository;
    private final CustomerRepository customerRepository;

    private final CreateReservationRequestConverter createReservationRequestConverter;
    private final ReservationResponseConverter reservationResponseConverter;

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

    @Secured("ROLE_CUSTOMER")
    public long reserve(CreateReservationRequest createReservationRequest)
            throws NotFoundException, CarInRentException, ReservationCollisionException {
        logger.info("Creating reservation");
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

    @Secured({"ROLE_COMPANY", "ROLE_MANAGER"})
    public void close(long id) throws NotFoundException, UnauthorizedAccessException {
        logger.info("Closing reservation ID {}", id);
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.RESERVATION_NOT_FOUND));
        reservationValidator.validateClosing(reservation);

        reservation.setReturnedDate(LocalDate.now());
        reservation.setPrice(getPrice(reservation.getReceiveDate(), LocalDate.now(), reservation.getCar().getPrice()));
        reservationRepository.save(reservation);
    }

    @Secured("ROLE_CUSTOMER")
    public ReservationResponse getActiveByCustomer() throws NotFoundException {
        logger.info("Providing active reservation to customer");
        return reservationResponseConverter.from(reservationRepository.findByCustomerAndReturnedDateIsNull(getCustomer())
                .orElseThrow(() -> new NotFoundException(Constants.NO_ACTIVE_RESERVATION)));
    }

    @Secured("ROLE_CUSTOMER")
    public List<ReservationResponse> getClosedByCustomer() {
        logger.info("Providing all closed reservation to customer");
        return reservationRepository.findByCustomerAndReturnedDateIsNotNull(getCustomer()).stream()
                .map(reservationResponseConverter::from).collect(Collectors.toList());
    }

    @Secured("ROLE_CUSTOMER")
    public List<ReservationResponse> getAllByCustomer() {
        logger.info("Providing all reservation to customer");
        return reservationRepository.findByCustomer(getCustomer()).stream()
                .map(reservationResponseConverter::from).collect(Collectors.toList());
    }

    @Secured({"ROLE_COMPANY", "ROLE_MANAGER"})
    public List<ReservationResponse> getActiveBySiteId(long siteId) throws NotFoundException, UnauthorizedAccessException {
        logger.info("Providing active reservations of site ID {}", siteId);
        return reservationRepository.findByCarSiteAndReturnedDateIsNull(getSite(siteId)).stream()
                .map(reservationResponseConverter::from).collect(Collectors.toList());
    }

    @Secured({"ROLE_COMPANY", "ROLE_MANAGER"})
    public List<ReservationResponse> getClosedBySiteId(long siteId) throws NotFoundException, UnauthorizedAccessException {
        logger.info("Providing closed reservations of site ID {}", siteId);
        return reservationRepository.findByCarSiteAndReturnedDateIsNotNull(getSite(siteId)).stream()
                .map(reservationResponseConverter::from).collect(Collectors.toList());
    }

    @Secured({"ROLE_COMPANY", "ROLE_MANAGER"})
    public List<ReservationResponse> getAllBySiteId(long siteId) throws NotFoundException, UnauthorizedAccessException {
        logger.info("Providing all reservation of site ID {}", siteId);
        return reservationRepository.findByCarSite(getSite(siteId)).stream()
                .map(reservationResponseConverter::from).collect(Collectors.toList());
    }

    @Secured("ROLE_COMPANY")
    @Transactional
    public List<ReservationResponse> getActiveByCompany() {
        logger.info("Providing active reservations of company");
        return reservationRepository.findByCompanyAndReturnedDateIsNull(getCompany()).stream()
                .map(reservationResponseConverter::from).collect(Collectors.toList());
    }

    @Secured("ROLE_COMPANY")
    @Transactional
    public List<ReservationResponse> getClosedByCompany() {
        logger.info("Providing closed reservations of company");
        return reservationRepository.findByCompanyAndReturnedDateIsNotNull(getCompany()).stream()
                .map(reservationResponseConverter::from).collect(Collectors.toList());
    }

    @Secured("ROLE_COMPANY")
    @Transactional
    public List<ReservationResponse> getAllByCompany() {
        logger.info("Providing all reservation of company");
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
}
