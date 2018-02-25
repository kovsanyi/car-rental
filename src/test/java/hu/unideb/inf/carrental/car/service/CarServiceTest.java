package hu.unideb.inf.carrental.car.service;

import hu.unideb.inf.carrental.car.resource.model.CarResponse;
import hu.unideb.inf.carrental.car.resource.model.CreateCarRequest;
import hu.unideb.inf.carrental.commons.domain.car.enumeration.CarCategory;
import hu.unideb.inf.carrental.commons.domain.car.enumeration.FuelType;
import hu.unideb.inf.carrental.commons.exception.CarInRentException;
import hu.unideb.inf.carrental.commons.exception.NotFoundException;
import hu.unideb.inf.carrental.commons.exception.UnauthorizedAccessException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:beforeTestRun.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:afterTestRun.sql")
})
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CarServiceTest {

    @Test
    public void saveShouldBeSuccess() throws Exception {
        setAuth("companyWithSites");
        CreateCarRequest createCarRequest = new CreateCarRequest(1L, CarCategory.SUV, "Brand", "Model", 1, 2018, FuelType.Petrol, 1.0F, 1, 1, 1);
        CarResponse carResponse = new CarResponse(5L, 1L, CarCategory.SUV, "Brand", "Model", 1, 2018, FuelType.Petrol, 1.0F, 1, 1, 1);

        assert carService.save(createCarRequest) == 5;
        assert carService.getById(5L).equals(carResponse);
    }

    @Test(expected = UnauthorizedAccessException.class)
    public void saveShouldThrowUnauthorizedAccessException() throws Exception {
        setAuth("company");
        CreateCarRequest createCarRequest = new CreateCarRequest(1L, CarCategory.SUV, "Brand", "Model", 1, 2018, FuelType.Petrol, 1.0F, 1, 1, 1);
        carService.save(createCarRequest);
    }

    @Test
    public void deleteShouldBeSuccess() throws Exception {
        setAuth("companyWithCars");
        carService.delete(1L);
    }

    @Test(expected = NotFoundException.class)
    public void deleteShouldThrowNotFoundException() throws Exception {
        setAuth("companyWithCars");
        carService.delete(1L);
        carService.delete(1L);
    }

    @Test(expected = UnauthorizedAccessException.class)
    public void deleteShouldThrowUnauthorizedAccessException() throws Exception {
        setAuth("company");
        carService.delete(1L);
    }

    @Test(expected = CarInRentException.class)
    public void deleteShouldThrowCarInRentException() throws Exception {
        setAuth("companyWithCars");
        carService.delete(4L);
    }

    @Test
    public void getByIdShouldBeSuccess() throws Exception {
        assert carService.getById(1L).getBrand().equals("Brand1");
        assert carService.getById(2L).getBrand().equals("Brand2");
        assert carService.getById(3L).getBrand().equals("Brand3");
        assert carService.getById(4L).getBrand().equals("Brand4");
    }

    @Test(expected = NotFoundException.class)
    public void getByIdShouldThrowNotFoundException() throws Exception {
        carService.getById(100L);
    }

    @Test
    public void getByCompanyIdShouldBeSuccess() throws Exception {
        assert carService.getByCompanyId(1L).size() == 0;
        assert carService.getByCompanyId(2L).size() == 0;
        assert carService.getByCompanyId(3L).size() == 4;
    }

    @Test(expected = NotFoundException.class)
    public void getByCompanyIdShouldThrowNotFoundException() throws NotFoundException {
        carService.getByCompanyId(100L);
    }

    @Test
    public void getByCompanyNameShouldBeSuccess() throws Exception {
        assert carService.getByCompanyName("Company").size() == 0;
        assert carService.getByCompanyName("Company With Sites").size() == 0;
        assert carService.getByCompanyName("Company With Cars").size() == 4;
    }

    @Test(expected = NotFoundException.class)
    public void getByCompanyNameShouldThrowNotFoundException() throws Exception {
        carService.getByCompanyName("Company Does Not Exists");
    }

    @Test
    public void getBySiteIdShouldBeSuccess() throws Exception {
        assert carService.getBySiteId(1L).size() == 0;
        assert carService.getBySiteId(2L).size() == 0;
        assert carService.getBySiteId(3L).size() == 2;
        assert carService.getBySiteId(4L).size() == 2;
    }

    @Test(expected = NotFoundException.class)
    public void getBySiteIdShouldThrowNotFoundException() throws Exception {
        carService.getBySiteId(100L);
    }

    @Test
    public void getAvailableBySiteIdShouldBeSuccess() throws Exception {
        assert carService.getAvailableBySiteId(1L).size() == 0;
        assert carService.getAvailableBySiteId(2L).size() == 0;
        assert carService.getAvailableBySiteId(3L).size() == 1;
        assert carService.getAvailableBySiteId(4L).size() == 1;
    }

    @Test(expected = NotFoundException.class)
    public void getAvailableBySiteIdShouldThrowNotFoundException() throws Exception {
        carService.getAvailableBySiteId(100L);
    }

    @Test
    public void getByParamsShouldBeSuccess() {
        assert carService.getByParams(null, null, null, null, null, null).size() == 4;
        assert carService.getByParams(null, null, null, 5, null, null).size() == 4;

        assert carService.getByParams(CarCategory.SUV, null, null, null, null, null).size() == 1;
        assert carService.getByParams(CarCategory.SUV, null, null, null, null, null).get(0).getBrand().equals("Brand1");

        assert carService.getByParams(null, "Brand1", null, null, null, null).size() == 1;
        assert carService.getByParams(null, "Brand1", null, null, null, null).get(0).getModel().equals("Model1");

        assert carService.getByParams(null, null, null, null, null, 11111).size() == 1;
        assert carService.getByParams(null, null, null, null, null, 11111).get(0).getBrand().equals("Brand1");

        assert carService.getByParams(null, null, null, null, 44444, null).size() == 1;
        assert carService.getByParams(null, null, null, null, 44444, null).get(0).getBrand().equals("Brand4");
    }

    @Test
    public void getUsedCarCategoriesShouldBeSuccess() {
        assert carService.getUsedCarCategories().size() == 4;
    }

    @Test
    public void getUsedBrandsShouldBeSuccess() {
        assert carService.getUsedBrands().size() == 4;
    }

    @Test
    public void getUsedBrandsByCategoryShouldBeSuccess() {
        assert carService.getUsedBrandsByCategory(CarCategory.SUV).size() == 1;
        assert carService.getUsedBrandsByCategory(CarCategory.Luxury).size() == 1;
        assert carService.getUsedBrandsByCategory(CarCategory.Minivan).size() == 1;
        assert carService.getUsedBrandsByCategory(CarCategory.Compact).size() == 1;
    }

    private void setAuth(String username) {
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(username.toLowerCase(), "password");
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Autowired
    private CarService carService;

    @Autowired
    AuthenticationManager authenticationManager;
}