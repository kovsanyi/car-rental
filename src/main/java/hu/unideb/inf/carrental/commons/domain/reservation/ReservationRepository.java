package hu.unideb.inf.carrental.commons.domain.reservation;

import hu.unideb.inf.carrental.commons.domain.car.Car;
import hu.unideb.inf.carrental.commons.domain.company.Company;
import hu.unideb.inf.carrental.commons.domain.customer.Customer;
import hu.unideb.inf.carrental.commons.domain.site.Site;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends CrudRepository<Reservation, Long> {
    Optional<Reservation> findById(long id);

    Optional<Reservation> findByCustomerAndReturnedDateIsNull(Customer customer);

    List<Reservation> findByCustomerAndReturnedDateIsNotNull(Customer customer);

    List<Reservation> findByCustomer(Customer customer);

    List<Reservation> findByCarSiteAndReturnedDateIsNull(Site site);

    List<Reservation> findByCarSiteAndReturnedDateIsNotNull(Site site);

    List<Reservation> findByCar(Car car);

    List<Reservation> findByCarSite(Site site);

    List<Reservation> findByCompany(Company company);

    List<Reservation> findByCompanyAndReturnedDateIsNull(Company company);

    List<Reservation> findByCompanyAndReturnedDateIsNotNull(Company company);

    Optional<Reservation> findByCarAndReturnedDateIsNull(Car car);

    List<Reservation> findByCompanyAndReturnedDateBetween(Company company, LocalDate from, LocalDate to);
}
