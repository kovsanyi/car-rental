package hu.unideb.inf.carrental.car.service.delete;

import hu.unideb.inf.carrental.commons.domain.car.Car;
import hu.unideb.inf.carrental.commons.domain.car.CarRepository;
import hu.unideb.inf.carrental.commons.domain.carimage.CarImageRepository;
import hu.unideb.inf.carrental.commons.exception.CarInRentException;
import hu.unideb.inf.carrental.reservation.service.delete.DeleteReservation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteCar {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteCar.class);

    private final DeleteReservation deleteReservation;
    private final CarRepository carRepository;
    private final CarImageRepository carImageRepository;

    @Autowired
    public DeleteCar(DeleteReservation deleteReservation, CarRepository carRepository, CarImageRepository carImageRepository) {
        this.deleteReservation = deleteReservation;
        this.carRepository = carRepository;
        this.carImageRepository = carImageRepository;
    }

    @Transactional(rollbackFor = CarInRentException.class)
    public void delete(Car car) throws CarInRentException {
        LOGGER.trace("Deleting car");
        deleteReservation.deleteCar(car);
        carImageRepository.deleteByCar(car);
        carRepository.delete(car);
    }
}
