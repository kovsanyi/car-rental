package hu.unideb.inf.carrental.company.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.unideb.inf.carrental.commons.constant.Constants;
import hu.unideb.inf.carrental.commons.domain.company.Company;
import hu.unideb.inf.carrental.commons.domain.user.User;
import hu.unideb.inf.carrental.commons.domain.user.enumeration.UserRole;
import hu.unideb.inf.carrental.commons.exception.enumeration.ExceptionType;
import hu.unideb.inf.carrental.commons.model.CreatedResponse;
import hu.unideb.inf.carrental.commons.model.ErrorResponse;
import hu.unideb.inf.carrental.commons.model.SuccessResponse;
import hu.unideb.inf.carrental.company.resource.model.CreateCompanyRequest;
import hu.unideb.inf.carrental.company.resource.model.UpdateCompanyRequest;
import hu.unideb.inf.carrental.company.service.CompanyService;
import hu.unideb.inf.carrental.company.service.converter.CompanyResponseConverter;
import hu.unideb.inf.carrental.company.service.converter.CreateCompanyRequestConverter;
import hu.unideb.inf.carrental.company.service.converter.UpdateCompanyRequestConverter;
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

import java.util.Collections;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CompanyResourceTest {
    //TODO delete if company has reservation(s)

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        companyService.save(testCreateCompanyRequest());
    }

    @Test
    public void saveShouldBeSuccess() throws Exception {
        CreateCompanyRequest request = testCreateCompanyRequest();
        request.setUserUsername("test2");
        request.setUserEmail("test2@mail.com");
        request.setName("Company2");
        request.setEmail("company2@mail.com");

        assert mvc.perform(
                post(Constants.PATH_COMPANY + CompanyResource.SAVE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new CreatedResponse(2L)));
    }

    @Test
    public void saveWhenUsernameAlreadyExists() throws Exception {
        CreateCompanyRequest request = testCreateCompanyRequest();
        request.setUserEmail("test2@mail.com");
        request.setName("Company2");
        request.setEmail("company2@mail.com");

        assert mvc.perform(
                post(Constants.PATH_COMPANY + CompanyResource.SAVE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andExpect(status().isNotAcceptable())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.USERNAME_EXISTS, Constants.USERNAME_ALREADY_EXISTS)));
    }

    @Test
    public void saveWhenEmailAlreadyExists() throws Exception {
        CreateCompanyRequest request = testCreateCompanyRequest();
        request.setUserUsername("test2");
        request.setName("Company2");
        request.setEmail("company2@mail.com");

        assert mvc.perform(
                post(Constants.PATH_COMPANY + CompanyResource.SAVE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andExpect(status().isNotAcceptable())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.EMAIL_EXISTS, Constants.EMAIL_ALREADY_EXISTS)));
    }

    @Test
    public void saveWhenNameAlreadyExists() throws Exception {
        CreateCompanyRequest request = testCreateCompanyRequest();
        request.setUserUsername("test2");
        request.setUserEmail("test2@mail.com");
        request.setEmail("company2@mail.com");

        assert mvc.perform(
                post(Constants.PATH_COMPANY + CompanyResource.SAVE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andExpect(status().isNotAcceptable())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.NAME_EXISTS, Constants.COMPANY_NAME_ALREADY_EXISTS)));
    }

    @Test
    public void saveWhenCompanyEmailAlreadyExists() throws Exception {
        CreateCompanyRequest request = testCreateCompanyRequest();
        request.setUserUsername("test2");
        request.setUserEmail("test2@mail.com");
        request.setName("Company2");

        assert mvc.perform(
                post(Constants.PATH_COMPANY + CompanyResource.SAVE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andExpect(status().isNotAcceptable())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.COMPANY_EMAIL_EXISTS, Constants.COMPANY_EMAIL_ALREADY_EXISTS)));
    }

    @Test
    public void updateShouldBeSuccess() throws Exception {
        UpdateCompanyRequest request = testUpdateCompanyRequest();

        assert mvc.perform(
                put(Constants.PATH_COMPANY + CompanyResource.UPDATE)
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
                delete(Constants.PATH_COMPANY + CompanyResource.DELETE)
                        .with(httpBasic(testUser().getUsername(), testUser().getPassword())))
                .andExpect(status().isAccepted())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new SuccessResponse()));
    }

    @Test
    public void getShouldBeSuccess() throws Exception {
        assert mvc.perform(
                get(Constants.PATH_COMPANY + CompanyResource.GET_ROOT)
                        .with(httpBasic(testUser().getUsername(), testUser().getPassword())))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(companyResponseConverter.from(testCompany())));
    }

    @Test
    public void getByIdShouldBeSuccess() throws Exception {
        assert mvc.perform(
                get(Constants.PATH_COMPANY + CompanyResource.GET_BY_ID, testCompany().getId())
                        .with(httpBasic(testUser().getUsername(), testUser().getPassword())))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(companyResponseConverter.from(testCompany())));
    }

    @Test
    public void getByIdWhenInputInvalid() throws Exception {
        assert mvc.perform(
                get(Constants.PATH_COMPANY + CompanyResource.GET_BY_ID, 2)
                        .with(httpBasic(testUser().getUsername(), testUser().getPassword())))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.NOT_FOUND, Constants.INVALID_COMPANY_ID)));
    }

    @Test
    public void getByNameShouldBeSuccess() throws Exception {
        assert mvc.perform(
                get(Constants.PATH_COMPANY + CompanyResource.GET_BY_NAME, testCompany().getName())
                        .with(httpBasic(testUser().getUsername(), testUser().getPassword())))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(companyResponseConverter.from(testCompany())));
    }

    @Test
    public void getByUsernameWhenInputInvalid() throws Exception {
        assert mvc.perform(
                get(Constants.PATH_COMPANY + CompanyResource.GET_BY_NAME, "invalidName")
                        .with(httpBasic(testUser().getUsername(), testUser().getPassword())))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.NOT_FOUND, Constants.COMPANY_NOT_FOUND)));
    }

    @Test
    public void getAll() throws Exception {
        assert mvc.perform(
                get(Constants.PATH_COMPANY + CompanyResource.GET_ALL)
                        .with(httpBasic(testUser().getUsername(), testUser().getPassword())))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(Collections.singleton(companyResponseConverter.from(testCompany()))));
    }

    private UpdateCompanyRequest testUpdateCompanyRequest() {
        UpdateCompanyRequest request = new UpdateCompanyRequest();
        request.setName(testCompany().getName());
        request.setEmail(testCompany().getEmail());
        request.setFullName(testCompany().getFullName());
        request.setPhoneNumber(testCompany().getPhoneNumber());
        request.setZipCode(testCompany().getZipCode());
        request.setCity(testCompany().getCity());
        request.setAddress(testCompany().getAddress());
        return request;
    }

    private CreateCompanyRequest testCreateCompanyRequest() {
        CreateCompanyRequest request = new CreateCompanyRequest();
        request.setUserUsername(testCompany().getUser().getUsername());
        request.setUserPassword(testCompany().getUser().getPassword());
        request.setUserEmail(testCompany().getUser().getEmail());
        request.setName(testCompany().getName());
        request.setEmail(testCompany().getEmail());
        request.setFullName(testCompany().getFullName());
        request.setPhoneNumber(testCompany().getPhoneNumber());
        request.setZipCode(testCompany().getZipCode());
        request.setCity(testCompany().getCity());
        request.setAddress(testCompany().getAddress());
        return request;
    }

    private Company testCompany() {
        Company company = new Company();
        company.setId(1L);
        company.setUser(testUser());
        company.setName("Company");
        company.setEmail("company@mail.com");
        company.setFullName("Test");
        company.setPhoneNumber("123");
        company.setZipCode(1234);
        company.setCity("City");
        company.setAddress("Address");
        return company;
    }

    private User testUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setPassword("password");
        user.setEmail("test@mail.com");
        user.setRole(UserRole.ROLE_COMPANY);
        return user;
    }

    private String toJson(Object object) throws JsonProcessingException {
        return objectMapper().writeValueAsString(object);
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyResourceTest.class);


    @Autowired
    CompanyService companyService;

    @Autowired
    CreateCompanyRequestConverter createCompanyRequestConverter;

    @Autowired
    UpdateCompanyRequestConverter updateCompanyRequestConverter;

    @Autowired
    CompanyResponseConverter companyResponseConverter;

    @Autowired
    WebApplicationContext context;

    private MockMvc mvc;

    @Bean
    private ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}