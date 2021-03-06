package hu.unideb.inf.carrental.commons.domain.carimage;

import hu.unideb.inf.carrental.commons.domain.car.Car;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CarImageRepository extends CrudRepository<CarImage, Long> {
    Optional<CarImage> findById(long id);

    List<CarImage> findByCar(Car car);

    List<CarImage> findByCarId(long id);

    void deleteByCar(Car car);
}
