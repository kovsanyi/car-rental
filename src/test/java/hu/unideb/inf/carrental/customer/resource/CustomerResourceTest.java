package hu.unideb.inf.carrental.customer.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.unideb.inf.carrental.commons.constant.Constants;
import hu.unideb.inf.carrental.commons.domain.user.enumeration.UserRole;
import hu.unideb.inf.carrental.commons.exception.enumeration.ExceptionType;
import hu.unideb.inf.carrental.commons.model.CreatedResponse;
import hu.unideb.inf.carrental.commons.model.ErrorResponse;
import hu.unideb.inf.carrental.commons.model.SuccessResponse;
import hu.unideb.inf.carrental.customer.resource.model.CreateCustomerRequest;
import hu.unideb.inf.carrental.customer.resource.model.CustomerResponse;
import hu.unideb.inf.carrental.customer.resource.model.UpdateCustomerRequest;
import hu.unideb.inf.carrental.customer.service.CustomerService;
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
import java.util.List;

import static hu.unideb.inf.carrental.customer.resource.CustomerResource.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:beforeTestRun.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:afterTestRun.sql")
})
public class CustomerResourceTest {
    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    public void saveShouldBeSuccess() throws Exception {
        CreateCustomerRequest createCustomerRequest = new CreateCustomerRequest("newCustomer".toLowerCase(), "password", "newcustomer@mail.com", "New Customer", "11111111111", 1111, "City", "Address");
        CustomerResponse customerResponse = new CustomerResponse(10L, "newCustomer".toLowerCase(), "newcustomer@mail.com", UserRole.ROLE_CUSTOMER.toString(), "New Customer", "11111111111", 1111, "City", "Address");

        assert mvc.perform(
                post(PATH + SAVE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(createCustomerRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new CreatedResponse(5L)));

        assert mvc.perform(
                get(PATH + GET_BY_ID, 5))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(customerResponse));
    }

    @Test
    public void saveWhenUsernameAlreadyExists() throws Exception {
        CreateCustomerRequest createCustomerRequest = new CreateCustomerRequest("customer".toLowerCase(), "password", "newcustomer@mail.com", "New Customer", "11111111111", 1111, "City", "Address");

        assert mvc.perform(
                post(PATH + SAVE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(createCustomerRequest)))
                .andExpect(status().isNotAcceptable())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.USERNAME_EXISTS, Constants.USERNAME_ALREADY_EXISTS)));
    }

    @Test
    public void saveWhenEmailAlreadyExists() throws Exception {
        CreateCustomerRequest createCustomerRequest = new CreateCustomerRequest("newCustomer".toLowerCase(), "password", "customer@mail.com", "New Customer", "11111111111", 1111, "City", "Address");

        assert mvc.perform(
                post(PATH + SAVE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(createCustomerRequest)))
                .andExpect(status().isNotAcceptable())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.EMAIL_EXISTS, Constants.EMAIL_ALREADY_EXISTS)));
    }

    @Test
    public void updateShouldBeSuccess() throws Exception {
        UpdateCustomerRequest updateCustomerRequest = new UpdateCustomerRequest("Updated Customer", "11111111112", 1112, "City1", "Address1");
        CustomerResponse customerResponse = new CustomerResponse(4L, "customer", "customer@mail.com", UserRole.ROLE_CUSTOMER.toString(), "Updated Customer", "11111111112", 1112, "City1", "Address1");

        assert mvc.perform(
                put(PATH + UPDATE)
                        .with(withAuth("customer"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(updateCustomerRequest)))
                .andExpect(status().isAccepted())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new SuccessResponse()));

        assert mvc.perform(
                get(PATH + GET_BY_ID, 1))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(customerResponse));
    }

    @Test
    public void deleteShouldBeSuccess() throws Exception {
        assert mvc.perform(
                delete(PATH + DELETE)
                        .with(withAuth("customer")))
                .andExpect(status().isAccepted())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new SuccessResponse()));

        assert mvc.perform(
                delete(PATH + DELETE)
                        .with(withAuth("customerClosedReservation")))
                .andExpect(status().isAccepted())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new SuccessResponse()));
    }

    @Test
    public void deleteWhenAlreadyDeleted() throws Exception {
        assert mvc.perform(
                delete(PATH + DELETE)
                        .with(withAuth("customer")))
                .andExpect(status().isAccepted())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new SuccessResponse()));

        mvc.perform(
                delete(PATH + DELETE)
                        .with(withAuth("customer")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void deleteWhenCarInRent() throws Exception {
        System.err.println(mvc.perform(
                delete(PATH + DELETE)
                        .with(withAuth("customerActiveReservation")))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString());
        assert mvc.perform(
                delete(PATH + DELETE)
                        .with(withAuth("customerActiveReservation")))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.CAR_IN_RENT, Constants.CAR_STILL_IN_RENT)));
    }

    @Test
    public void getShouldBeSuccess() throws Exception {
        assert mvc.perform(
                get(PATH + GET_ROOT)
                        .with(withAuth("customer")))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(customerService.getById(1L)));

        assert mvc.perform(
                get(PATH + GET_ROOT)
                        .with(withAuth("customerActiveReservation")))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(customerService.getById(2L)));

        assert mvc.perform(
                get(PATH + GET_ROOT)
                        .with(withAuth("customerClosedReservation")))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(customerService.getById(3L)));

        assert mvc.perform(
                get(PATH + GET_ROOT)
                        .with(withAuth("customerReservation")))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(customerService.getById(4L)));
    }

    @Test
    public void getByIdShouldBeSuccess() throws Exception {
        assert mvc.perform(
                get(PATH + GET_BY_ID, 1))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(customerService.getById(1L)));

        assert mvc.perform(
                get(PATH + GET_BY_ID, 2))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(customerService.getById(2L)));

        assert mvc.perform(
                get(PATH + GET_BY_ID, 3))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(customerService.getById(3L)));

        assert mvc.perform(
                get(PATH + GET_BY_ID, 4))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(customerService.getById(4L)));
    }

    @Test
    public void getByIdWhenInputInvalid() throws Exception {
        assert mvc.perform(
                get(PATH + GET_BY_ID, 100))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.NOT_FOUND, Constants.CUSTOMER_NOT_FOUND)));
    }

    @Test
    public void getByUserIdShouldBeSuccess() throws Exception {
        assert mvc.perform(
                get(PATH + GET_BY_USER_ID, 4))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(customerService.getByUserId(4L)));

        assert mvc.perform(
                get(PATH + GET_BY_USER_ID, 5))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(customerService.getByUserId(5L)));

        assert mvc.perform(
                get(PATH + GET_BY_USER_ID, 6))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(customerService.getByUserId(6L)));

        assert mvc.perform(
                get(PATH + GET_BY_USER_ID, 7))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(customerService.getByUserId(7L)));
    }

    @Test
    public void getByUserIdWhenInputInvalid() throws Exception {
        assert mvc.perform(
                get(PATH + GET_BY_USER_ID, 1))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.NOT_FOUND, Constants.CUSTOMER_NOT_FOUND)));
    }

    @Test
    public void getByUsernameShouldBeSuccess() throws Exception {
        assert mvc.perform(
                get(PATH + GET_BY_USERNAME, "customer"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(customerService.getByUserId(4L)));

        assert mvc.perform(
                get(PATH + GET_BY_USERNAME, "customerActiveReservation"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(customerService.getByUserId(5L)));

        assert mvc.perform(
                get(PATH + GET_BY_USERNAME, "customerClosedReservation"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(customerService.getByUserId(6L)));

        assert mvc.perform(
                get(PATH + GET_BY_USERNAME, "customerReservation"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(customerService.getByUserId(7L)));
    }

    @Test
    public void getByUsernameWhenInputInvalid() throws Exception {
        assert mvc.perform(
                get(PATH + GET_BY_USERNAME, "company"))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.NOT_FOUND, Constants.CUSTOMER_NOT_FOUND)));
    }

    private RequestPostProcessor withAuth(String username) {
        return httpBasic(username.toLowerCase(), "password");
    }

    private String toJson(Object object) throws JsonProcessingException {
        return objectMapper().writeValueAsString(object);
    }

    private List<CustomerResponse> fromJsonList(String json) throws IOException {
        return objectMapper().readValue(json, objectMapper().getTypeFactory().constructCollectionType(List.class, CustomerResponse.class));
    }

    @Bean
    private ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Autowired
    private CustomerService customerService;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    private String PATH = Constants.PATH_CUSTOMER;
}