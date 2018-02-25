package hu.unideb.inf.carrental.car.resource;

import hu.unideb.inf.carrental.car.resource.model.CreateCarRequest;
import hu.unideb.inf.carrental.car.service.CarService;
import hu.unideb.inf.carrental.commons.constant.Constants;
import hu.unideb.inf.carrental.commons.domain.car.enumeration.CarCategory;
import hu.unideb.inf.carrental.commons.domain.car.enumeration.FuelType;
import hu.unideb.inf.carrental.commons.exception.CarInRentException;
import hu.unideb.inf.carrental.commons.exception.NotFoundException;
import hu.unideb.inf.carrental.commons.exception.UnauthorizedAccessException;
import hu.unideb.inf.carrental.commons.model.CreatedResponse;
import hu.unideb.inf.carrental.commons.model.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(Constants.PATH_CAR)
public class CarResource {
    private final CarService carService;

    @Autowired
    public CarResource(CarService carService) {
        this.carService = carService;
    }

    @PostMapping(SAVE)
    public ResponseEntity<?> save(@Valid @RequestBody CreateCarRequest createCarRequest)
            throws UnauthorizedAccessException {
        return new ResponseEntity<>(new CreatedResponse(carService.save(createCarRequest)), HttpStatus.CREATED);
    }

    @DeleteMapping(DELETE)
    public ResponseEntity<?> delete(@PathVariable("id") long id)
            throws NotFoundException, UnauthorizedAccessException, CarInRentException {
        carService.delete(id);
        return new ResponseEntity<>(new SuccessResponse(), HttpStatus.ACCEPTED);
    }

    @GetMapping(GET_BY_ID)
    public ResponseEntity<?> getById(@PathVariable("id") long id) throws NotFoundException {
        return new ResponseEntity<>(carService.getById(id), HttpStatus.OK);
    }

    @GetMapping(GET_BY_COMPANY_ID)
    public ResponseEntity<?> getByCompanyId(@PathVariable("companyId") long siteId) throws NotFoundException {
        return new ResponseEntity<>(carService.getByCompanyId(siteId), HttpStatus.OK);
    }

    @GetMapping(GET_BY_COMPANY_NAME)
    public ResponseEntity<?> getByCompanyId(@PathVariable("companyName") String companyName) throws NotFoundException {
        return new ResponseEntity<>(carService.getByCompanyName(companyName), HttpStatus.OK);
    }

    @GetMapping(GET_BY_SITE_ID)
    public ResponseEntity<?> getBySiteId(@PathVariable("siteId") long siteId) throws NotFoundException {
        return new ResponseEntity<>(carService.getBySiteId(siteId), HttpStatus.OK);
    }

    @GetMapping(GET_AVAILABLE_BY_SITE_ID)
    public ResponseEntity<?> getAvailableBySiteId(@PathVariable("siteId") long siteId) throws NotFoundException {
        return new ResponseEntity<>(carService.getAvailableBySiteId(siteId), HttpStatus.OK);
    }

    @GetMapping(SEARCH)
    public ResponseEntity<?> getByParams(
            @RequestParam(value = "category", required = false) CarCategory category,
            @RequestParam(value = "brand", required = false) String brand,
            @RequestParam(value = "fuelType", required = false) FuelType fuelType,
            @RequestParam(value = "seatNumber", required = false) Integer seatNumber,
            @RequestParam(value = "minPrice", required = false) Integer minPrice,
            @RequestParam(value = "maxPrice", required = false) Integer maxPrice) {
        return new ResponseEntity<>(carService.getByParams(category, brand, fuelType, seatNumber, minPrice, maxPrice),
                HttpStatus.OK);
    }

    @GetMapping(GET_ALL_CAR_CATEGORY)
    public ResponseEntity<?> getAllCarCategory() {
        return new ResponseEntity<>(carService.getAllCarCategory(), HttpStatus.OK);
    }

    @GetMapping(GET_ALL_FUEL_TYPE)
    public ResponseEntity<?> getAllFuelType() {
        return new ResponseEntity<>(carService.getAllFuelType(), HttpStatus.OK);
    }

    @GetMapping(GET_USED_CAR_CATEGORIES)
    public ResponseEntity<?> getUsedCarCategories() {
        return new ResponseEntity<>(carService.getUsedCarCategories(), HttpStatus.OK);
    }

    @GetMapping(GET_USED_BRANDS_BY_CAR_CATEGORY)
    public ResponseEntity<?> getUsedBrandsByCategory(@PathVariable("carCategory") CarCategory category) {
        return new ResponseEntity<>(carService.getUsedBrandsByCategory(category), HttpStatus.OK);
    }

    @GetMapping(GET_USED_BRANDS)
    public ResponseEntity<?> getUsedBrands() {
        return new ResponseEntity<>(carService.getUsedBrands(), HttpStatus.OK);
    }

    public static final String SAVE = "/save";
    public static final String DELETE = "/delete/{id}";
    public static final String GET_BY_ID = "/id/{id}";
    public static final String GET_BY_COMPANY_ID = "/companyId/{companyId}";
    public static final String GET_BY_COMPANY_NAME = "/companyName/{companyName}";
    public static final String GET_BY_SITE_ID = "/siteId/{siteId}";
    public static final String GET_AVAILABLE_BY_SITE_ID = "/siteId/{siteId}/available";
    public static final String SEARCH = "/search";
    public static final String GET_ALL_CAR_CATEGORY = "/category/all";
    public static final String GET_ALL_FUEL_TYPE = "/fuelType/all";
    public static final String GET_USED_CAR_CATEGORIES = "/category/used";
    public static final String GET_USED_BRANDS_BY_CAR_CATEGORY = "/category/{carCategory}/brand";
    public static final String GET_USED_BRANDS = "/brand/all";
}
