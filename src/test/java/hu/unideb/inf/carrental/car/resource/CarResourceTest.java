package hu.unideb.inf.carrental.car.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.unideb.inf.carrental.car.resource.model.CarResponse;
import hu.unideb.inf.carrental.car.resource.model.CreateCarRequest;
import hu.unideb.inf.carrental.car.service.CarService;
import hu.unideb.inf.carrental.commons.constant.Constants;
import hu.unideb.inf.carrental.commons.domain.car.enumeration.CarCategory;
import hu.unideb.inf.carrental.commons.domain.car.enumeration.FuelType;
import hu.unideb.inf.carrental.commons.exception.enumeration.ExceptionType;
import hu.unideb.inf.carrental.commons.model.CreatedResponse;
import hu.unideb.inf.carrental.commons.model.ErrorResponse;
import hu.unideb.inf.carrental.commons.model.SuccessResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static hu.unideb.inf.carrental.car.resource.CarResource.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:beforeTestRun.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:afterTestRun.sql")
})
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CarResourceTest {

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    public void saveShouldBeSuccess() throws Exception {
        CreateCarRequest createCarRequest = new CreateCarRequest(1L, CarCategory.SUV, "Brand", "Model", 1, 2018, FuelType.Petrol, 1.0F, 1, 1, 1);
        CarResponse carResponse = new CarResponse(5L, 1L, CarCategory.SUV, "Brand", "Model", 1, 2018, FuelType.Petrol, 1.0F, 1, 1, 1);

        assert mvc.perform(
                post(PATH + SAVE)
                        .with(withAuth("companyWithSites"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(createCarRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new CreatedResponse(5L)));

        assert mvc.perform(
                get(PATH + GET_BY_ID, 5))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(carResponse));
    }

    @Test
    public void saveWhenUnauthorized() throws Exception {
        CreateCarRequest createCarRequest = new CreateCarRequest(1L, CarCategory.SUV, "Brand", "Model", 1, 2018, FuelType.Petrol, 1.0F, 1, 1, 1);
        assert mvc.perform(
                post(PATH + SAVE)
                        .with(withAuth("company"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(createCarRequest)))
                .andExpect(status().isUnauthorized())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.UNAUTHORIZED, Constants.NO_RIGHTS)));
    }

    @Test
    public void deleteShouldBeSuccess() throws Exception {
        assert mvc.perform(
                delete(PATH + DELETE, 1L)
                        .with(withAuth("companyWithCars")))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new SuccessResponse()));
    }

    @Test
    public void deleteWhenNotFound() throws Exception {
        assert mvc.perform(
                delete(PATH + DELETE, 1L)
                        .with(withAuth("companyWithCars")))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new SuccessResponse()));
        assert mvc.perform(
                delete(PATH + DELETE, 1L)
                        .with(withAuth("companyWithCars")))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.NOT_FOUND, Constants.CAR_NOT_FOUND)));
    }

    @Test
    public void deleteWhenUnauthorized() throws Exception {
        assert mvc.perform(
                delete(PATH + DELETE, 1L)
                        .with(withAuth("company")))
                .andExpect(status().isUnauthorized())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.UNAUTHORIZED, Constants.NO_RIGHTS)));
    }

    @Test
    public void deleteWhenCarInRent() throws Exception {
        assert mvc.perform(
                delete(PATH + DELETE, 4L)
                        .with(withAuth("companyWithCars")))
                .andExpect(status().isNotModified())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.CAR_IN_RENT, Constants.CAR_STILL_IN_RENT)));
    }

    @Test
    public void getByIdShouldBeSuccess() throws Exception {
        assert mvc.perform(
                get(PATH + GET_BY_ID, 1L))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(carService.getById(1L)));
        assert mvc.perform(
                get(PATH + GET_BY_ID, 2L))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(carService.getById(2L)));
        assert mvc.perform(
                get(PATH + GET_BY_ID, 3L))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(carService.getById(3L)));
        assert mvc.perform(
                get(PATH + GET_BY_ID, 4L))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(carService.getById(4L)));
    }

    @Test
    public void getByIdWhenInvalid() throws Exception {
        assert mvc.perform(
                get(PATH + GET_BY_ID, 100L))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.NOT_FOUND, Constants.CAR_NOT_FOUND)));
    }

    @Test
    public void getByCompanyIdShouldBeSuccess() throws Exception {
        assert fromJsonList(
                mvc.perform(
                        get(PATH + GET_BY_COMPANY_ID, 1L))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString()
        ).size() == 0;
        assert fromJsonList(
                mvc.perform(
                        get(PATH + GET_BY_COMPANY_ID, 2L))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString()
        ).size() == 0;
        assert fromJsonList(
                mvc.perform(
                        get(PATH + GET_BY_COMPANY_ID, 3L))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString()
        ).size() == 4;
    }

    @Test
    public void getByCompanyIdWhenInvalid() throws Exception {
        assert mvc.perform(
                get(PATH + GET_BY_COMPANY_ID, 100L))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.NOT_FOUND, Constants.COMPANY_NOT_FOUND)));
    }

    @Test
    public void getByCompanyNameShouldBeSuccess() throws Exception {
        assert fromJsonList(
                mvc.perform(
                        get(PATH + GET_BY_COMPANY_NAME, "Company"))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString()
        ).size() == 0;
        assert fromJsonList(
                mvc.perform(
                        get(PATH + GET_BY_COMPANY_NAME, "Company With Sites"))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString()
        ).size() == 0;
        assert fromJsonList(
                mvc.perform(
                        get(PATH + GET_BY_COMPANY_NAME, "Company With Cars"))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString()
        ).size() == 4;
    }

    @Test
    public void getByCompanyNameWhenInvalid() throws Exception {
        assert mvc.perform(
                get(PATH + GET_BY_COMPANY_NAME, "Company Does Not Exists"))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.NOT_FOUND, Constants.COMPANY_NOT_FOUND)));
    }

    @Test
    public void getBySiteIdShouldBeSuccess() throws Exception {
        assert fromJsonList(
                mvc.perform(
                        get(PATH + GET_BY_SITE_ID, 1L))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString()
        ).size() == 0;
        assert fromJsonList(
                mvc.perform(
                        get(PATH + GET_BY_SITE_ID, 2L))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString()
        ).size() == 0;
        assert fromJsonList(
                mvc.perform(
                        get(PATH + GET_BY_SITE_ID, 3L))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString()
        ).size() == 2;
        assert fromJsonList(
                mvc.perform(
                        get(PATH + GET_BY_SITE_ID, 4L))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString()
        ).size() == 2;
    }

    @Test
    public void getBySiteIdWhenInvalid() throws Exception {
        assert mvc.perform(
                get(PATH + GET_BY_SITE_ID, 100L))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.NOT_FOUND, Constants.SITE_NOT_FOUND)));
    }

    @Test
    public void getAvailableBySiteIdShouldBeSuccess() throws Exception {
        assert fromJsonList(
                mvc.perform(
                        get(PATH + GET_AVAILABLE_BY_SITE_ID, 1L))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString()
        ).size() == 0;
        assert fromJsonList(
                mvc.perform(
                        get(PATH + GET_AVAILABLE_BY_SITE_ID, 2L))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString()
        ).size() == 0;
        assert fromJsonList(
                mvc.perform(
                        get(PATH + GET_AVAILABLE_BY_SITE_ID, 3L))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString()
        ).size() == 2;
        assert fromJsonList(
                mvc.perform(
                        get(PATH + GET_AVAILABLE_BY_SITE_ID, 4L))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString()
        ).size() == 2;
    }

    @Test
    public void getAvailableWhenInvalid() throws Exception {
        assert mvc.perform(
                get(PATH + GET_AVAILABLE_BY_SITE_ID, 100L))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.NOT_FOUND, Constants.SITE_NOT_FOUND)));
    }

    @Test
    public void getByParamsShouldBeSuccess() throws Exception {
        assert fromJsonList(
                mvc.perform(
                        get(PATH + SEARCH))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString()
        ).size() == 4;
        assert fromJsonList(
                mvc.perform(
                        get(PATH + SEARCH + "?seatNumber=5"))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString()
        ).size() == 4;

        List<CarResponse> searchSUV = fromJsonList(
                mvc.perform(
                        get(PATH + SEARCH + "?category=" + CarCategory.SUV))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString());
        assert searchSUV.size() == 1;
        assert searchSUV.get(0).equals(carService.getById(1));

        List<CarResponse> searchBrand1 = fromJsonList(
                mvc.perform(
                        get(PATH + SEARCH + "?brand=Brand1"))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString());
        assert searchBrand1.size() == 1;
        assert searchBrand1.get(0).equals(carService.getById(1));

        List<CarResponse> searchMaxPrice = fromJsonList(
                mvc.perform(
                        get(PATH + SEARCH + "?maxPrice=11111"))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString());
        assert searchMaxPrice.size() == 1;
        assert searchMaxPrice.get(0).equals(carService.getById(1));


        List<CarResponse> searchMinPrice = fromJsonList(
                mvc.perform(
                        get(PATH + SEARCH + "?minPrice=44444"))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString());
        assert searchMinPrice.size() == 1;
        assert searchMinPrice.get(0).equals(carService.getById(4));
    }

    @Test
    public void getUsedCarCategoriesShouldBeSuccess() throws Exception {
        List<String> categories = objectMapper().readValue(
                mvc.perform(
                        get(PATH + GET_USED_CAR_CATEGORIES))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString(),
                objectMapper().getTypeFactory().constructCollectionType(List.class, String.class));
        assert categories.size() == 4;
    }

    @Test
    public void getUsedBrandsShouldBeSuccess() throws Exception {
        List<String> brands = objectMapper().readValue(
                mvc.perform(
                        get(PATH + GET_USED_BRANDS))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString(),
                objectMapper().getTypeFactory().constructCollectionType(List.class, String.class));
        assert brands.size() == 4;
    }

    @Test
    public void getUsedBrandsByCategoryShouldBeSuccess() throws Exception {
        assert mvc.perform(
                get(PATH + GET_USED_BRANDS_BY_CAR_CATEGORY, CarCategory.SUV))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(Collections.singleton("Brand1")));
        assert mvc.perform(
                get(PATH + GET_USED_BRANDS_BY_CAR_CATEGORY, CarCategory.Luxury))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(Collections.singleton("Brand2")));
        assert mvc.perform(
                get(PATH + GET_USED_BRANDS_BY_CAR_CATEGORY, CarCategory.Minivan))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(Collections.singleton("Brand3")));
        assert mvc.perform(
                get(PATH + GET_USED_BRANDS_BY_CAR_CATEGORY, CarCategory.Compact))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(Collections.singleton("Brand4")));
    }

    private RequestPostProcessor withAuth(String username) {
        return httpBasic(username.toLowerCase(), "password");
    }

    private String toJson(Object object) throws JsonProcessingException {
        return objectMapper().writeValueAsString(object);
    }

    private List<CarResponse> fromJsonList(String json) throws IOException {
        return objectMapper().readValue(json, objectMapper().getTypeFactory().constructCollectionType(List.class, CarResponse.class));
    }

    @Bean
    private ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Autowired
    private CarService carService;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    private String PATH = Constants.PATH_CAR;
}