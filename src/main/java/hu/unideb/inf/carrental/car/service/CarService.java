package hu.unideb.inf.carrental.car.service;

import hu.unideb.inf.carrental.car.resource.model.CarResponse;
import hu.unideb.inf.carrental.car.resource.model.CreateCarRequest;
import hu.unideb.inf.carrental.car.service.converter.CarResponseConverter;
import hu.unideb.inf.carrental.car.service.converter.CreateCarRequestConverter;
import hu.unideb.inf.carrental.car.service.delete.DeleteCar;
import hu.unideb.inf.carrental.car.service.validator.CarValidator;
import hu.unideb.inf.carrental.commons.constant.Constants;
import hu.unideb.inf.carrental.commons.domain.car.Car;
import hu.unideb.inf.carrental.commons.domain.car.CarRepository;
import hu.unideb.inf.carrental.commons.domain.car.enumeration.CarCategory;
import hu.unideb.inf.carrental.commons.domain.car.enumeration.FuelType;
import hu.unideb.inf.carrental.commons.exception.CarInRentException;
import hu.unideb.inf.carrental.commons.exception.NotFoundException;
import hu.unideb.inf.carrental.commons.exception.UnauthorizedAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CarService.class);

    private final CarRepository carRepository;
    private final CarValidator carValidator;
    private final DeleteCar deleteCar;

    private final CreateCarRequestConverter createCarRequestConverter;
    private final CarResponseConverter carResponseConverter;

    @Autowired
    public CarService(CarRepository carRepository, CarValidator carValidator, DeleteCar deleteCar,
                      CreateCarRequestConverter createCarRequestConverter, CarResponseConverter carResponseConverter) {
        this.carRepository = carRepository;
        this.carValidator = carValidator;
        this.deleteCar = deleteCar;
        this.createCarRequestConverter = createCarRequestConverter;
        this.carResponseConverter = carResponseConverter;
    }

    //NoSuchElement if ID is invalid!
    @Secured({"ROLE_COMPANY", "ROLE_MANAGER"})
    public long save(CreateCarRequest createCarRequest) throws UnauthorizedAccessException {
        LOGGER.info("Saving car");
        Car car = createCarRequestConverter.from(createCarRequest);
        carValidator.validate(car);
        return carRepository.save(car).getId();
    }

    @Secured({"ROLE_COMPANY", "ROLE_MANAGER"})
    public void delete(long id) throws NotFoundException, UnauthorizedAccessException, CarInRentException {
        LOGGER.info("Deleting car by ID {}", id);
        Car car = carRepository.findById(id).orElseThrow(() -> new NotFoundException(Constants.CAR_NOT_FOUND));
        carValidator.validate(car);
        deleteCar.delete(car);
    }

    public CarResponse getById(long id) throws NotFoundException {
        LOGGER.info("Providing car by ID {}", id);
        return carResponseConverter.from(carRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.CAR_NOT_FOUND)));
    }

    public List<CarResponse> getByCompanyId(long companyId) {
        LOGGER.info("Providing cars by company ID {}", companyId);
        return carRepository.findBySiteCompanyId(companyId).stream().map(carResponseConverter::from)
                .collect(Collectors.toList());
    }

    public List<CarResponse> getByCompanyName(String companyName) {
        LOGGER.info("Providing cars by company named {}", companyName);
        return carRepository.findBySiteCompanyName(companyName).stream().map(carResponseConverter::from)
                .collect(Collectors.toList());
    }

    public List<CarResponse> getBySiteId(long siteId) {
        LOGGER.info("Providing cars by site ID {}", siteId);
        return carRepository.findBySiteId(siteId).stream().map(carResponseConverter::from)
                .collect(Collectors.toList());
    }

    public List<CarResponse> getAvailableBySiteId(long siteId) {
        LOGGER.info("Providing available cars by site ID {}", siteId);
        return carRepository.getAvailableCars(siteId).stream().map(carResponseConverter::from)
                .collect(Collectors.toList());
    }

    // SECURITY ISSUE! Replace it as soon as possible!
    public List<CarResponse> getByParams(CarCategory category, String brand, FuelType fuelType, Integer seatNumber,
                                         Integer minPrice, Integer maxPrice) {
        String paramCategory = category == null ? "%" : category.toString();
        String paramBrand = brand == null ? "%" : brand;
        String paramFuelType = fuelType == null ? "%" : fuelType.toString();
        String paramSeatNumber = seatNumber == null ? "%" : seatNumber.toString();
        Integer paramMinPrice = minPrice == null ? 0 : minPrice;
        Integer paramMaxPrice = maxPrice == null ? Constants.CAR_MAX_PRICE : maxPrice;
        return carRepository.findByParams(paramCategory, paramBrand, paramFuelType, paramSeatNumber, paramMinPrice,
                paramMaxPrice)
                .stream().map(carResponseConverter::from).collect(Collectors.toList());
    }

    public List<?> getAllCarCategory() {
        return Arrays.asList(CarCategory.values());
    }

    public List<?> getAllFuelType() {
        return Arrays.asList(FuelType.values());
    }

    public List<?> getUsedCarCategories() {
        return carRepository.getDistinctCategory();
    }

    public List<?> getUsedBrands() {
        return carRepository.getDistinctBrand();
    }

    public List<?> getUsedBrandsByCategory(CarCategory category) {
        return carRepository.getDistinctBrandByCategory(category.toString());
    }
}
