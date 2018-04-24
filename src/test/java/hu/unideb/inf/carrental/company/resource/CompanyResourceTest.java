package hu.unideb.inf.carrental.company.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.unideb.inf.carrental.commons.constant.Constants;
import hu.unideb.inf.carrental.commons.exception.enumeration.ExceptionType;
import hu.unideb.inf.carrental.commons.model.CreatedResponse;
import hu.unideb.inf.carrental.commons.model.ErrorResponse;
import hu.unideb.inf.carrental.commons.model.SuccessResponse;
import hu.unideb.inf.carrental.company.resource.model.CompanyResponse;
import hu.unideb.inf.carrental.company.resource.model.CreateCompanyRequest;
import hu.unideb.inf.carrental.company.resource.model.UpdateCompanyRequest;
import hu.unideb.inf.carrental.company.service.CompanyService;
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

import static hu.unideb.inf.carrental.company.resource.CompanyResource.*;
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
public class CompanyResourceTest {
    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    public void saveShouldBeSuccess() throws Exception {
        CreateCompanyRequest createCompanyRequest = new CreateCompanyRequest("newCompany".toLowerCase(), "password", "newcompany@mail.com", "New Company", "newcompany@mail.com", "New Company", "11111111111", 1111, "City", "Address");
        CompanyResponse companyResponse = new CompanyResponse(4L, 10L, "New Company", "newcompany@mail.com", "New Company", "11111111111", 1111, "City", "Address");

        assert mvc.perform(
                post(PATH + SAVE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(createCompanyRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new CreatedResponse(4L)));

        assert mvc.perform(
                get(PATH + GET_BY_ID, 4))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(companyResponse));
    }

    @Test
    public void saveWhenUsernameAlreadyExists() throws Exception {
        CreateCompanyRequest createCompanyRequest = new CreateCompanyRequest("company".toLowerCase(), "password", "newcompany@mail.com", "New Company", "newcompany@mail.com", "New Company", "11111111111", 1111, "City", "Address");

        assert mvc.perform(
                post(PATH + SAVE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(createCompanyRequest)))
                .andExpect(status().isNotAcceptable())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.USERNAME_EXISTS, Constants.USERNAME_ALREADY_EXISTS)));
    }

    @Test
    public void saveWhenEmailAlreadyExists() throws Exception {
        CreateCompanyRequest createCompanyRequest = new CreateCompanyRequest("newCompany".toLowerCase(), "password", "company@mail.com", "New Company", "newcompany@mail.com", "New Company", "11111111111", 1111, "City", "Address");

        assert mvc.perform(
                post(PATH + SAVE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(createCompanyRequest)))
                .andExpect(status().isNotAcceptable())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.EMAIL_EXISTS, Constants.EMAIL_ALREADY_EXISTS)));
    }

    @Test
    public void saveWhenNameAlreadyExists() throws Exception {
        CreateCompanyRequest createCompanyRequest = new CreateCompanyRequest("newCompany".toLowerCase(), "password", "newcompany@mail.com", "Company", "newcompany@mail.com", "New Company", "11111111111", 1111, "City", "Address");

        assert mvc.perform(
                post(PATH + SAVE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(createCompanyRequest)))
                .andExpect(status().isNotAcceptable())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.NAME_EXISTS, Constants.COMPANY_NAME_ALREADY_EXISTS)));
    }

    @Test
    public void saveWhenCompanyEmailAlreadyExists() throws Exception {
        CreateCompanyRequest createCompanyRequest = new CreateCompanyRequest("newCompany".toLowerCase(), "password", "newcompany@mail.com", "New Company", "company@mail.com", "New Company", "11111111111", 1111, "City", "Address");

        assert mvc.perform(
                post(PATH + SAVE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(createCompanyRequest)))
                .andExpect(status().isNotAcceptable())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.COMPANY_EMAIL_EXISTS, Constants.COMPANY_EMAIL_ALREADY_EXISTS)));
    }

    @Test
    public void updateShouldBeSuccess() throws Exception {
        UpdateCompanyRequest updateCompanyRequest = new UpdateCompanyRequest("New Company", "newcompany@mail.com", "New Company", "11111111111", 1111, "City", "Address");

        assert mvc.perform(
                put(PATH + UPDATE)
                        .with(withAuth("company"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(updateCompanyRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new SuccessResponse()));
    }

    @Test
    public void updateWhenNameAlreadyExists() throws Exception {
        UpdateCompanyRequest updateCompanyRequest = new UpdateCompanyRequest("Company", "newcompany@mail.com", "New Company", "11111111111", 1111, "City", "Address");

        assert mvc.perform(
                put(PATH + UPDATE)
                        .with(withAuth("companyWithSites"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(updateCompanyRequest)))
                .andExpect(status().isNotAcceptable())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.NAME_EXISTS, Constants.COMPANY_NAME_ALREADY_EXISTS)));
    }

    @Test
    public void updateWhenCompanyEmailAlreadyExists() throws Exception {
        UpdateCompanyRequest updateCompanyRequest = new UpdateCompanyRequest("New Company", "company@mail.com", "New Company", "11111111111", 1111, "City", "Address");

        assert mvc.perform(
                put(PATH + UPDATE)
                        .with(withAuth("companyWithSites"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(updateCompanyRequest)))
                .andExpect(status().isNotAcceptable())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.COMPANY_EMAIL_EXISTS, Constants.COMPANY_EMAIL_ALREADY_EXISTS)));
    }

    @Test
    public void deleteShouldBeSuccess() throws Exception {
        assert mvc.perform(
                delete(PATH + DELETE)
                        .with(withAuth("company")))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new SuccessResponse()));
    }

    @Test
    public void deleteWhenCompanyHasSites() throws Exception {
        assert mvc.perform(
                delete(PATH + DELETE)
                        .with(withAuth("companyWithSites")))
                .andExpect(status().isNotModified())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.COLLISION, Constants.COMPANY_HAS_SITES)));
    }

    @Test
    public void getShouldBeSuccess() throws Exception {
        assert mvc.perform(
                get(PATH + GET_ROOT)
                        .with(withAuth("company")))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(companyService.getById(1L)));
        assert mvc.perform(
                get(PATH + GET_ROOT)
                        .with(withAuth("companyWithSites")))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(companyService.getById(2L)));
        assert mvc.perform(
                get(PATH + GET_ROOT)
                        .with(withAuth("companyWithCars")))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(companyService.getById(3L)));
    }

    @Test
    public void getByIdShouldBeSuccess() throws Exception {
        assert mvc.perform(
                get(PATH + GET_BY_ID, 1))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(companyService.getById(1L)));
        assert mvc.perform(
                get(PATH + GET_BY_ID, 2))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(companyService.getById(2L)));
        assert mvc.perform(
                get(PATH + GET_BY_ID, 3))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(companyService.getById(3L)));
    }

    @Test
    public void getByIdWhenInvalid() throws Exception {
        assert mvc.perform(
                get(PATH + GET_BY_ID, 100))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.NOT_FOUND, Constants.INVALID_COMPANY_ID)));
    }

    @Test
    public void getByNameShouldBeSuccess() throws Exception {
        assert mvc.perform(
                get(PATH + GET_BY_NAME, "Company"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(companyService.getById(1L)));
        assert mvc.perform(
                get(PATH + GET_BY_NAME, "Company With Sites"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(companyService.getById(2L)));
        assert mvc.perform(
                get(PATH + GET_BY_NAME, "Company With Cars"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(companyService.getById(3L)));
    }

    @Test
    public void getByNameWhenInvalid() throws Exception {
        assert mvc.perform(
                get(PATH + GET_BY_NAME, "Company Does Not Exists"))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.NOT_FOUND, Constants.COMPANY_NOT_FOUND)));
    }

    @Test
    public void getAllShouldBeSuccess() throws Exception {
        assert fromJsonList(
                mvc.perform(
                        get(PATH + GET_ALL))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString()
        ).size() == 3;
    }

    private RequestPostProcessor withAuth(String username) {
        return httpBasic(username.toLowerCase(), "password");
    }

    private String toJson(Object object) throws JsonProcessingException {
        return objectMapper().writeValueAsString(object);
    }

    private List<CompanyResponse> fromJsonList(String json) throws IOException {
        return objectMapper().readValue(json, objectMapper().getTypeFactory().constructCollectionType(List.class, CompanyResponse.class));
    }

    @Bean
    private ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Autowired
    private CompanyService companyService;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    private String PATH = Constants.PATH_COMPANY;
}