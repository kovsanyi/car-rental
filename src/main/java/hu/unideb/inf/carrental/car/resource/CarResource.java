package hu.unideb.inf.carrental.car.resource;

import hu.unideb.inf.carrental.car.resource.model.CreateCarRequest;
import hu.unideb.inf.carrental.car.service.CarService;
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
@RequestMapping("/api/car")
public class CarResource {
    private final CarService carService;

    @Autowired
    public CarResource(CarService carService) {
        this.carService = carService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@Valid @RequestBody CreateCarRequest createCarRequest)
            throws UnauthorizedAccessException {
        return new ResponseEntity<>(new CreatedResponse(carService.save(createCarRequest)), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id)
            throws NotFoundException, UnauthorizedAccessException, CarInRentException {
        carService.delete(id);
        return new ResponseEntity<>(new SuccessResponse(), HttpStatus.ACCEPTED);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") long id) throws NotFoundException {
        return new ResponseEntity<>(carService.getById(id), HttpStatus.OK);
    }

    @GetMapping("/companyId/{companyId}")
    public ResponseEntity<?> getByCompanyId(@PathVariable("companyId") long siteId) {
        return new ResponseEntity<>(carService.getByCompanyId(siteId), HttpStatus.OK);
    }

    @GetMapping("/companyName/{companyName}")
    public ResponseEntity<?> getByCompanyId(@PathVariable("companyName") String companyName) {
        return new ResponseEntity<>(carService.getByCompanyName(companyName), HttpStatus.OK);
    }

    @GetMapping("/siteId/{siteId}")
    public ResponseEntity<?> getBySiteId(@PathVariable("siteId") long siteId) {
        return new ResponseEntity<>(carService.getBySiteId(siteId), HttpStatus.OK);
    }

    @GetMapping("/siteId/{siteId}/available")
    public ResponseEntity<?> getAvailableBySiteId(@PathVariable("siteId") long siteId) {
        return new ResponseEntity<>(carService.getAvailableBySiteId(siteId), HttpStatus.OK);
    }

    @GetMapping("/search")
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

    @GetMapping("/category/all")
    public ResponseEntity<?> getAllCarCategory() {
        return new ResponseEntity<>(carService.getAllCarCategory(), HttpStatus.OK);
    }

    @GetMapping("/fuelType/all")
    public ResponseEntity<?> getAllFuelType() {
        return new ResponseEntity<>(carService.getAllFuelType(), HttpStatus.OK);
    }

    @GetMapping("/category/used")
    public ResponseEntity<?> getUsedCarCategories() {
        return new ResponseEntity<>(carService.getUsedCarCategories(), HttpStatus.OK);
    }

    @GetMapping("/category/{carCategory}/brand")
    public ResponseEntity<?> getUsedBrandsByCategory(@PathVariable("carCategory") CarCategory category) {
        return new ResponseEntity<>(carService.getUsedBrandsByCategory(category), HttpStatus.OK);
    }

    @GetMapping("/brand/all")
    public ResponseEntity<?> getUsedBrands() {
        return new ResponseEntity<>(carService.getUsedBrands(), HttpStatus.OK);
    }
}
