package hu.unideb.inf.carrental.customer.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.unideb.inf.carrental.commons.constant.Constants;
import hu.unideb.inf.carrental.commons.domain.customer.Customer;
import hu.unideb.inf.carrental.commons.domain.user.User;
import hu.unideb.inf.carrental.commons.domain.user.enumeration.UserRole;
import hu.unideb.inf.carrental.commons.exception.enumeration.ExceptionType;
import hu.unideb.inf.carrental.commons.model.CreatedResponse;
import hu.unideb.inf.carrental.commons.model.ErrorResponse;
import hu.unideb.inf.carrental.commons.model.SuccessResponse;
import hu.unideb.inf.carrental.customer.resource.model.CreateCustomerRequest;
import hu.unideb.inf.carrental.customer.resource.model.UpdateCustomerRequest;
import hu.unideb.inf.carrental.customer.service.CustomerService;
import hu.unideb.inf.carrental.customer.service.converter.CreateCustomerRequestConverter;
import hu.unideb.inf.carrental.customer.service.converter.CustomerResponseConverter;
import hu.unideb.inf.carrental.customer.service.converter.UpdateCustomerRequestConverter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CustomerResourceTest {
    //TODO delete if customer has reservation

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        customerService.save(testCreateCustomerRequest());
    }

    @Test
    public void saveShouldBeSuccess() throws Exception {
        CreateCustomerRequest request = testCreateCustomerRequest();
        request.setUserUsername("test2");
        request.setUserEmail("test2@mail.com");

        assert mvc.perform(
                post(Constants.PATH_CUSTOMER + CustomerResource.SAVE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new CreatedResponse(2L)));
    }

    @Test
    public void saveWhenUsernameAlreadyExists() throws Exception {
        CreateCustomerRequest request = testCreateCustomerRequest();
        request.setUserEmail("test2@mail.com");

        assert mvc.perform(
                post(Constants.PATH_CUSTOMER + CustomerResource.SAVE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andExpect(status().isNotAcceptable())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.USERNAME_EXISTS, Constants.USERNAME_ALREADY_EXISTS)));
    }

    @Test
    public void saveWhenEmailAlreadyExists() throws Exception {
        CreateCustomerRequest request = testCreateCustomerRequest();
        request.setUserUsername("test2");

        assert mvc.perform(
                post(Constants.PATH_CUSTOMER + CustomerResource.SAVE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andExpect(status().isNotAcceptable())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.EMAIL_EXISTS, Constants.EMAIL_ALREADY_EXISTS)));
    }

    @Test
    public void updateShouldBeSuccess() throws Exception {
        UpdateCustomerRequest request = testUpdateCustomerRequest();

        assert mvc.perform(
                put(Constants.PATH_CUSTOMER + CustomerResource.UPDATE)
                        .with(httpBasic(testUser().getUsername(), testUser().getPassword()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andExpect(status().isAccepted())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new SuccessResponse()));
    }

    @Test
    public void deleteShouldBeSuccess() throws Exception {
        assert mvc.perform(
                delete(Constants.PATH_CUSTOMER + CustomerResource.DELETE)
                        .with(httpBasic(testUser().getUsername(), testUser().getPassword())))
                .andExpect(status().isAccepted())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new SuccessResponse()));
    }

    @Test
    public void getShouldBeSuccess() throws Exception {
        assert mvc.perform(
                get(Constants.PATH_CUSTOMER + CustomerResource.GET_ROOT)
                        .with(httpBasic(testUser().getUsername(), testUser().getPassword())))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(customerResponseConverter.from(testCustomer())));
    }

    @Test
    public void getByIdShouldBeSuccess() throws Exception {
        assert mvc.perform(
                get(Constants.PATH_CUSTOMER + CustomerResource.GET_BY_ID, testCustomer().getId())
                        .with(httpBasic(testUser().getUsername(), testUser().getPassword())))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(customerResponseConverter.from(testCustomer())));
    }

    @Test
    public void getByIdWhenInputInvalid() throws Exception {
        assert mvc.perform(
                get(Constants.PATH_CUSTOMER + CustomerResource.GET_BY_ID, 2)
                        .with(httpBasic(testUser().getUsername(), testUser().getPassword())))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.NOT_FOUND, Constants.CUSTOMER_NOT_FOUND)));
    }

    @Test
    public void getByUserIdShouldBeSuccess() throws Exception {
        assert mvc.perform(
                get(Constants.PATH_CUSTOMER + CustomerResource.GET_BY_USER_ID, testUser().getId())
                        .with(httpBasic(testUser().getUsername(), testUser().getPassword())))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(customerResponseConverter.from(testCustomer())));
    }

    @Test
    public void getByUserIdWhenInputInvalid() throws Exception {
        assert mvc.perform(
                get(Constants.PATH_CUSTOMER + CustomerResource.GET_BY_USER_ID, 2)
                        .with(httpBasic(testUser().getUsername(), testUser().getPassword())))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.NOT_FOUND, Constants.CUSTOMER_NOT_FOUND)));
    }

    @Test
    public void getByUsernameShouldBeSuccess() throws Exception {
        assert mvc.perform(
                get(Constants.PATH_CUSTOMER + CustomerResource.GET_BY_USERNAME, testUser().getUsername())
                        .with(httpBasic(testUser().getUsername(), testUser().getPassword())))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(customerResponseConverter.from(testCustomer())));
    }

    @Test
    public void getByUsernameWhenInputInvalid() throws Exception {
        assert mvc.perform(
                get(Constants.PATH_CUSTOMER + CustomerResource.GET_BY_USERNAME, "invalidUsername")
                        .with(httpBasic(testUser().getUsername(), testUser().getPassword())))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.NOT_FOUND, Constants.CUSTOMER_NOT_FOUND)));
    }

    private UpdateCustomerRequest testUpdateCustomerRequest() {
        UpdateCustomerRequest request = new UpdateCustomerRequest();
        request.setFullName(testCustomer().getFullName() + "updated");
        request.setPhoneNumber(testCustomer().getPhoneNumber());
        request.setZipCode(testCustomer().getZipCode());
        request.setCity(testCustomer().getCity());
        request.setAddress(testCustomer().getAddress());
        return request;
    }

    private CreateCustomerRequest testCreateCustomerRequest() {
        CreateCustomerRequest request = new CreateCustomerRequest();
        request.setUserUsername(testCustomer().getUser().getUsername());
        request.setUserPassword(testCustomer().getUser().getPassword());
        request.setUserEmail(testCustomer().getUser().getEmail());
        request.setFullName(testCustomer().getFullName());
        request.setPhoneNumber(testCustomer().getPhoneNumber());
        request.setZipCode(testCustomer().getZipCode());
        request.setCity(testCustomer().getCity());
        request.setAddress(testCustomer().getAddress());
        return request;
    }

    private Customer testCustomer() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setUser(testUser());
        customer.setFullName("Test");
        customer.setPhoneNumber("123");
        customer.setZipCode(1234);
        customer.setCity("City");
        customer.setAddress("Address");
        return customer;
    }


    private User testUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setPassword("password");
        user.setEmail("test@mail.com");
        user.setRole(UserRole.ROLE_CUSTOMER);
        return user;
    }

    private String toJson(Object object) throws JsonProcessingException {
        return objectMapper().writeValueAsString(object);
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerResourceTest.class);

    @Autowired
    CustomerService customerService;

    @Autowired
    CreateCustomerRequestConverter createCustomerRequestConverter;

    @Autowired
    UpdateCustomerRequestConverter updateCustomerRequestConverter;

    @Autowired
    CustomerResponseConverter customerResponseConverter;

    @Autowired
    WebApplicationContext context;

    private MockMvc mvc;

    @Bean
    private ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}