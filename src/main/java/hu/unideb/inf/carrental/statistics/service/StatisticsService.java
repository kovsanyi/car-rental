package hu.unideb.inf.carrental.statistics.service;

import hu.unideb.inf.carrental.commons.domain.company.Company;
import hu.unideb.inf.carrental.commons.domain.company.CompanyRepository;
import hu.unideb.inf.carrental.commons.domain.reservation.Reservation;
import hu.unideb.inf.carrental.commons.domain.reservation.ReservationRepository;
import hu.unideb.inf.carrental.commons.security.SecurityUtils;
import hu.unideb.inf.carrental.statistics.resource.model.IncomeByBrandResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatisticsService {
    private static final Logger logger = LoggerFactory.getLogger(StatisticsService.class);

    private final CompanyRepository companyRepository;
    private final ReservationRepository reservationRepository;

    @Autowired
    public StatisticsService(CompanyRepository companyRepository, ReservationRepository reservationRepository) {
        this.companyRepository = companyRepository;
        this.reservationRepository = reservationRepository;
    }

    /**
     * Returns a list of {@code IncomeByBrandResponse} which contains income by brand between two given dates.
     *
     * @param from  first date
     * @param until last date
     * @return income by {@code Car} brand
     * @see IncomeByBrandResponse
     * @see hu.unideb.inf.carrental.commons.domain.car.Car
     */
    @Secured("ROLE_COMPANY")
    public List<IncomeByBrandResponse> getIncomeByBrand(LocalDate from, LocalDate until) {
        logger.info("Providing statistics of income by brand");
        return reservationRepository.findByCompanyAndReturnedDateBetween(getCompany(), from, until).stream()
                .filter(e -> e.getCar() != null)
                .collect(Collectors.groupingBy(e -> e.getCar().getBrand(), Collectors.summingInt(Reservation::getPrice)))
                .entrySet().stream().map(e -> new IncomeByBrandResponse(e.getKey(), e.getValue())).collect(Collectors.toList());
    }

    private Company getCompany() {
        return companyRepository.findByUser(SecurityUtils.getLoggedInUser()).get();
    }
}
