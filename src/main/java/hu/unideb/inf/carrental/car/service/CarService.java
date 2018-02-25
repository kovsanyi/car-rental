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
import hu.unideb.inf.carrental.commons.domain.company.CompanyRepository;
import hu.unideb.inf.carrental.commons.domain.site.SiteRepository;
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
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service class for registering, searching and managing the cars.
 *
 * @see CreateCarRequest
 * @see CarResponse
 */
@Service
public class CarService {
    /**
     * Registers a new car to the specified site. The car will belong to the site specified in {@code CreateCarRequest}.<br>
     * <b>This method can only be called by a company owner or a manager.</b>
     *
     * @param createCarRequest a request object to register a car
     * @return the ID of the registered car
     * @throws UnauthorizedAccessException if the logged in user has no right to register a car to the specified site
     * @see hu.unideb.inf.carrental.site.service.SiteService
     */
    @Secured({"ROLE_COMPANY", "ROLE_MANAGER"})
    public long save(CreateCarRequest createCarRequest) throws UnauthorizedAccessException {
        LOGGER.info("Saving car");
        Car car = createCarRequestConverter.from(createCarRequest);
        carValidator.validate(car);
        return carRepository.save(car).getId();
    }

    /**
     * Deletes a car by ID.<br>
     * <b>This method can only be called by a company owner or a manager.</b>
     *
     * @param id ID of the car
     * @throws NotFoundException           if the given ID is invalid
     * @throws UnauthorizedAccessException if the logged in user has no right to delete from the site on which the car
     *                                     have been registered
     * @throws CarInRentException          if the car can not be deleted because it is under rent
     * @see hu.unideb.inf.carrental.reservation.service.ReservationService
     * @see hu.unideb.inf.carrental.site.service.SiteService
     */
    @Secured({"ROLE_COMPANY", "ROLE_MANAGER"})
    public void delete(long id) throws NotFoundException, UnauthorizedAccessException, CarInRentException {
        LOGGER.info("Deleting car by ID {}", id);
        Car car = carRepository.findById(id).orElseThrow(() -> new NotFoundException(Constants.CAR_NOT_FOUND));
        carValidator.validate(car);
        deleteCar.delete(car);
    }

    /**
     * Returns a car by ID.
     *
     * @param id ID of the car
     * @return a car by ID
     * @throws NotFoundException if the car ID is invalid
     */
    public CarResponse getById(long id) throws NotFoundException {
        LOGGER.info("Providing car by ID {}", id);
        return carResponseConverter.from(carRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.CAR_NOT_FOUND)));
    }

    /**
     * Returns all the cars by company ID.
     *
     * @param companyId ID of the company
     * @return all the cars by company ID
     * @throws NotFoundException if the company ID is invalid
     * @see hu.unideb.inf.carrental.company.service.CompanyService
     */
    public List<CarResponse> getByCompanyId(long companyId) throws NotFoundException {
        LOGGER.info("Providing cars by company ID {}", companyId);
        if (!companyRepository.existsById(companyId)) {
            throw new NotFoundException(Constants.COMPANY_NOT_FOUND);
        }
        return carRepository.findBySiteCompanyId(companyId).stream().map(carResponseConverter::from)
                .collect(Collectors.toList());
    }

    /**
     * Returns all the cars by company name.
     *
     * @param companyName name of the company
     * @return all the cars by company name
     * @throws NotFoundException if the name of the company does not exists
     * @see hu.unideb.inf.carrental.company.service.CompanyService
     */
    public List<CarResponse> getByCompanyName(String companyName) throws NotFoundException {
        LOGGER.info("Providing cars by company named {}", companyName);
        if (!companyRepository.existsByName(companyName)) {
            throw new NotFoundException(Constants.COMPANY_NOT_FOUND);
        }
        return carRepository.findBySiteCompanyName(companyName).stream().map(carResponseConverter::from)
                .collect(Collectors.toList());
    }

    /**
     * Returns all the cars by site ID.
     *
     * @param siteId ID of the site
     * @return all the cars by site ID
     * @throws NotFoundException if the site ID is invalid
     * @see hu.unideb.inf.carrental.site.service.SiteService
     */
    public List<CarResponse> getBySiteId(long siteId) throws NotFoundException {
        LOGGER.info("Providing cars by site ID {}", siteId);
        if (!siteRepository.existsById(siteId)) {
            throw new NotFoundException(Constants.SITE_NOT_FOUND);
        }
        return carRepository.findBySiteId(siteId).stream().map(carResponseConverter::from)
                .collect(Collectors.toList());
    }

    /**
     * Returns all the cars that does not under rent by site ID.
     *
     * @param siteId ID of the site
     * @return all the cars that does not under rent by site ID
     * @throws NotFoundException if the site ID is invalid
     * @see hu.unideb.inf.carrental.site.service.SiteService
     */
    public List<CarResponse> getAvailableBySiteId(long siteId) throws NotFoundException {
        LOGGER.info("Providing available cars by site ID {}", siteId);
        if (!siteRepository.existsById(siteId)) {
            throw new NotFoundException(Constants.SITE_NOT_FOUND);
        }
        return carRepository.getAvailableCars(siteId).stream().map(carResponseConverter::from)
                .collect(Collectors.toList());
    }

    /**
     * Searches for the cars that match the specified parameters. If one of the parameter values is <i>null</i> then
     * the parameter will not be involved in the search.
     *
     * @param category   category of the car
     * @param brand      brand of the car
     * @param fuelType   fuel type of the car
     * @param seatNumber seat number of the car
     * @param minPrice   minimum price of the car
     * @param maxPrice   maximum price of the car
     * @return the cars that match the specified parameters
     * @see CarCategory
     * @see FuelType
     */
    public List<CarResponse> getByParams(CarCategory category, String brand, FuelType fuelType, Integer seatNumber,
                                         Integer minPrice, Integer maxPrice) {
        String paramCategory = category == null ? "%" : category.toString();
        String paramBrand = brand == null ? "%" : brand;
        String paramFuelType = fuelType == null ? "%" : fuelType.toString();
        String paramSeatNumber = seatNumber == null ? "%" : seatNumber.toString();
        Integer paramMinPrice = minPrice == null ? 0 : minPrice;
        Integer paramMaxPrice = maxPrice == null ? Constants.CAR_MAX_PRICE : maxPrice;
        return carRepository
                .findByParams(paramCategory, paramBrand, paramFuelType, paramSeatNumber, paramMinPrice, paramMaxPrice)
                .stream().map(carResponseConverter::from).collect(Collectors.toList());
    }

    /**
     * Returns all the car categories.
     *
     * @return all the car categories
     * @see CarCategory
     */
    public List<CarCategory> getAllCarCategory() {
        return Arrays.asList(CarCategory.values());
    }

    /**
     * Returns all the fuel types.
     *
     * @return all the fuel types
     * @see FuelType
     */
    public List<FuelType> getAllFuelType() {
        return Arrays.asList(FuelType.values());
    }

    /**
     * Returns all the used car categories.
     *
     * @return all the used car categories
     * @see CarCategory
     */
    public Set<CarCategory> getUsedCarCategories() {
        return carRepository.getDistinctCategory().stream()
                .map(Object::toString)
                .map(CarCategory::valueOf)
                .collect(Collectors.toSet());
    }

    /**
     * Returns all the used car brands.
     *
     * @return all the used car brands
     */
    public Set<String> getUsedBrands() {
        return carRepository.getDistinctBrand().stream()
                .map(Object::toString)
                .collect(Collectors.toSet());
    }

    /**
     * Returns all the used brands by the given category.
     *
     * @param category category to specify brands
     * @return all the used brands by the given category
     * @see CarCategory
     */
    public Set<String> getUsedBrandsByCategory(CarCategory category) {
        return carRepository.getDistinctBrandByCategory(category.toString()).stream()
                .map(Object::toString)
                .collect(Collectors.toSet());
    }

    @Autowired
    public CarService(CompanyRepository companyRepository, SiteRepository siteRepository, CarRepository carRepository,
                      CarValidator carValidator, DeleteCar deleteCar, CreateCarRequestConverter createCarRequestConverter,
                      CarResponseConverter carResponseConverter) {
        this.companyRepository = companyRepository;
        this.siteRepository = siteRepository;
        this.carRepository = carRepository;
        this.carValidator = carValidator;
        this.deleteCar = deleteCar;
        this.createCarRequestConverter = createCarRequestConverter;
        this.carResponseConverter = carResponseConverter;
    }

    private final CompanyRepository companyRepository;
    private final SiteRepository siteRepository;
    private final CarRepository carRepository;
    private final CarValidator carValidator;
    private final DeleteCar deleteCar;

    private final CreateCarRequestConverter createCarRequestConverter;
    private final CarResponseConverter carResponseConverter;

    private static final Logger LOGGER = LoggerFactory.getLogger(CarService.class);
}
