package hu.unideb.inf.carrental.reservation.service.delete;

import hu.unideb.inf.carrental.commons.constant.Constants;
import hu.unideb.inf.carrental.commons.domain.car.Car;
import hu.unideb.inf.carrental.commons.domain.company.Company;
import hu.unideb.inf.carrental.commons.domain.customer.Customer;
import hu.unideb.inf.carrental.commons.domain.reservation.Reservation;
import hu.unideb.inf.carrental.commons.domain.reservation.ReservationRepository;
import hu.unideb.inf.carrental.commons.exception.CarInRentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DeleteReservation {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteReservation.class);

    private final ReservationRepository reservationRepository;

    @Autowired
    public DeleteReservation(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Transactional(rollbackFor = CarInRentException.class)
    public void deleteCar(Car car) throws CarInRentException {
        LOGGER.trace("Updating reservations due to car removing");
        if (reservationRepository.findByCarAndReturnedDateIsNull(car).isPresent()) {
            throw new CarInRentException(Constants.CAR_STILL_IN_RENT);
        } else {
            List<Reservation> reservations = reservationRepository.findByCar(car);
            for (Reservation r : reservations) {
                r.setCar(null);
                reservationRepository.save(r);
            }
        }
    }

    @Transactional(rollbackFor = CarInRentException.class)
    public void deleteCompany(Company company) throws CarInRentException {
        LOGGER.trace("Updating reservations due to company removing");
        if (!reservationRepository.findByCompanyAndReturnedDateIsNull(company).isEmpty()) {
            throw new CarInRentException(Constants.CAR_STILL_IN_RENT);
        } else {
            List<Reservation> reservations = reservationRepository.findByCompany(company);
            for (Reservation r : reservations) {
                r.setCompany(null);
                reservationRepository.save(r);
            }
        }
    }

    @Transactional(rollbackFor = CarInRentException.class)
    public void deleteCustomer(Customer customer) throws CarInRentException {
        LOGGER.trace("Updating reservations due to customer removing");
        if (reservationRepository.findByCustomerAndReturnedDateIsNull(customer).isPresent()) {
            throw new CarInRentException(Constants.CAR_STILL_IN_RENT);
        } else {
            List<Reservation> reservations = reservationRepository.findByCustomer(customer);
            for (Reservation r : reservations) {
                r.setCustomer(null);
                reservationRepository.save(r);
            }
        }
    }
}
