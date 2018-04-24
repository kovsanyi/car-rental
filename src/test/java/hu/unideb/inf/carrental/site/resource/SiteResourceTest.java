package hu.unideb.inf.carrental.site.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.unideb.inf.carrental.commons.constant.Constants;
import hu.unideb.inf.carrental.commons.exception.enumeration.ExceptionType;
import hu.unideb.inf.carrental.commons.model.CreatedResponse;
import hu.unideb.inf.carrental.commons.model.ErrorResponse;
import hu.unideb.inf.carrental.commons.model.SuccessResponse;
import hu.unideb.inf.carrental.site.resource.model.CreateSiteRequest;
import hu.unideb.inf.carrental.site.resource.model.SiteResponse;
import hu.unideb.inf.carrental.site.resource.model.UpdateSiteRequest;
import hu.unideb.inf.carrental.site.service.SiteService;
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

import static hu.unideb.inf.carrental.site.resource.SiteResource.*;
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
public class SiteResourceTest {

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @DirtiesContext
    @Test
    public void saveShouldBeSuccessWhenManagerExists() throws Exception {
        CreateSiteRequest createSiteRequest = new CreateSiteRequest("manager", "new_site@mail.com", "99999999999", 9999, "City", "Address", "OpeningHours");
        SiteResponse siteResponse = new SiteResponse(5L, 1L, 1L, "manager", "new_site@mail.com", "99999999999", 9999, "City", "Address", "OpeningHours");

        assert mvc.perform(
                post(PATH + SAVE)
                        .with(withAuth("company"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(createSiteRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new CreatedResponse(5L)));

        assert mvc.perform(
                get(PATH + GET_BY_ID, 5))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(siteResponse));
    }

    @DirtiesContext
    @Test
    public void saveShouldBeSuccessWhenManagerNull() throws Exception {
        CreateSiteRequest createSiteRequest = new CreateSiteRequest(null, "new_site@mail.com", "99999999999", 9999, "City", "Address", "OpeningHours");
        SiteResponse siteResponse = new SiteResponse(5L, 1L, null, null, "new_site@mail.com", "99999999999", 9999, "City", "Address", "OpeningHours");

        assert mvc.perform(
                post(PATH + SAVE)
                        .with(withAuth("company"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(createSiteRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new CreatedResponse(5L)));

        assert mvc.perform(
                get(PATH + GET_BY_ID, 5))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(siteResponse));
    }

    @Test
    public void saveWhenInputInvalid() throws Exception {
        CreateSiteRequest createSiteRequest = new CreateSiteRequest("invalidManager".toLowerCase(), "new_site@mail.com", "99999999999", 9999, "City", "Address", "OpeningHours");

        assert mvc.perform(
                post(PATH + SAVE)
                        .with(withAuth("company"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(createSiteRequest)))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.NOT_FOUND, Constants.MANAGER_NOT_FOUND)));
    }

    @Test
    public void updateByCompanyShouldBeSuccess() throws Exception {
        UpdateSiteRequest updateSiteRequest = new UpdateSiteRequest(1L, "new_site@mail.com", "99999999999", 9999, "City", "Address", "OpeningHours");
        SiteResponse siteResponse = new SiteResponse(1L, 2L, null, null, "new_site@mail.com", "99999999999", 9999, "City", "Address", "OpeningHours");

        assert mvc.perform(
                put(PATH + UPDATE)
                        .with(withAuth("companyWithSites"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(updateSiteRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new SuccessResponse()));

        assert mvc.perform(
                get(PATH + GET_BY_ID, 1))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(siteResponse));
    }

    @Test
    public void updateByManagerShouldBeSuccess() throws Exception {
        UpdateSiteRequest updateSiteRequest = new UpdateSiteRequest(3L, "new_site@mail.com", "99999999999", 9999, "City", "Address", "OpeningHours");
        SiteResponse siteResponse = new SiteResponse(3L, 3L, 2L, "managerwithsite", "new_site@mail.com", "99999999999", 9999, "City", "Address", "OpeningHours");

        assert mvc.perform(
                put(PATH + UPDATE)
                        .with(withAuth("managerWithSite"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(updateSiteRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new SuccessResponse()));

        assert mvc.perform(
                get(PATH + GET_BY_ID, 3))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(siteResponse));
    }

    public void updateByCompanyWhenIdInvalid() throws Exception {
        UpdateSiteRequest updateSiteRequest = new UpdateSiteRequest(100L, "new_site@mail.com", "99999999999", 9999, "City", "Address", "OpeningHours");

        assert mvc.perform(
                put(PATH + UPDATE)
                        .with(withAuth("companyWithSites"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(updateSiteRequest)))
                .andExpect(status().isNotAcceptable())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.NOT_FOUND, Constants.SITE_NOT_FOUND)));
    }

    public void updateByManagerWhenIdInvalid() throws Exception {
        UpdateSiteRequest updateSiteRequest = new UpdateSiteRequest(100L, "new_site@mail.com", "99999999999", 9999, "City", "Address", "OpeningHours");

        assert mvc.perform(
                put(PATH + UPDATE)
                        .with(withAuth("managerWithSite"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(updateSiteRequest)))
                .andExpect(status().isNotAcceptable())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.NOT_FOUND, Constants.SITE_NOT_FOUND)));
    }

    public void updateByManagerWhenUnauthorized() throws Exception {
        UpdateSiteRequest updateSiteRequest = new UpdateSiteRequest(1L, "new_site@mail.com", "99999999999", 9999, "City", "Address", "OpeningHours");

        assert mvc.perform(
                put(PATH + UPDATE)
                        .with(withAuth("managerWithSite"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(updateSiteRequest)))
                .andExpect(status().isUnauthorized())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.UNAUTHORIZED, Constants.NO_RIGHTS)));
    }

    @Test
    public void deleteShouldBeSuccess() throws Exception {
        assert mvc.perform(
                delete(PATH + DELETE, 1)
                        .with(withAuth("companyWithSites")))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new SuccessResponse()));

        assert mvc.perform(
                get(PATH + GET_BY_ID, 1))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.NOT_FOUND, Constants.SITE_NOT_FOUND)));
    }

    @Test
    public void deleteWhenIdInvalid() throws Exception {
        assert mvc.perform(
                delete(PATH + DELETE, 100)
                        .with(withAuth("company")))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.NOT_FOUND, Constants.SITE_NOT_FOUND)));
    }

    @Test
    public void deleteWhenUnauthorized() throws Exception {
        assert mvc.perform(
                delete(PATH + DELETE, 3)
                        .with(withAuth("company")))
                .andExpect(status().isUnauthorized())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.UNAUTHORIZED, Constants.NO_RIGHTS)));
    }

    @Test
    public void deleteWhenCarInRent() throws Exception {
        assert mvc.perform(
                delete(PATH + DELETE, 3)
                        .with(withAuth("companyWithCars")))
                .andExpect(status().isNotModified())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.COLLISION, Constants.SITE_HAS_CARS)));
    }

    @Test
    public void setManagerByIdShouldBeSuccess() throws Exception {
        assert mvc.perform(
                put(PATH + SET_MANAGER_BY_ID, 1, 1)
                        .with(withAuth("companyWithSites")))
                .andExpect(status().isAccepted())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new SuccessResponse()));
    }

    @Test
    public void setManagerByIdWhenInvalid() throws Exception {
        assert mvc.perform(
                put(PATH + SET_MANAGER_BY_ID, 1, 100)
                        .with(withAuth("companyWithSites")))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.NOT_FOUND, Constants.MANAGER_NOT_FOUND)));
    }

    @Test
    public void setManagerByIdWhenUnauthorized() throws Exception {
        assert mvc.perform(
                put(PATH + SET_MANAGER_BY_ID, 1, 1)
                        .with(withAuth("company")))
                .andExpect(status().isUnauthorized())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.UNAUTHORIZED, Constants.NO_RIGHTS)));
    }

    @Test
    public void setManagerByIdWhenCollision() throws Exception {
        assert mvc.perform(
                put(PATH + SET_MANAGER_BY_ID, 1, 2)
                        .with(withAuth("companyWithSites")))
                .andExpect(status().isNotModified())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.MANAGER_COLLISION, Constants.MANAGER_ALREADY_SITE_MANAGER)));
    }

    @Test
    public void setManagerByUserIdShouldBeSuccess() throws Exception {
        assert mvc.perform(
                put(PATH + SET_MANAGER_BY_USER_ID, 1, 8)
                        .with(withAuth("companyWithSites")))
                .andExpect(status().isAccepted())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new SuccessResponse()));
    }

    @Test
    public void setManagerByUserIdWhenInvalid() throws Exception {
        assert mvc.perform(
                put(PATH + SET_MANAGER_BY_USER_ID, 1, 100)
                        .with(withAuth("companyWithSites")))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.NOT_FOUND, Constants.MANAGER_NOT_FOUND)));
    }

    @Test
    public void setManagerByUserIdWhenUnauthorized() throws Exception {
        assert mvc.perform(
                put(PATH + SET_MANAGER_BY_USER_ID, 1, 8)
                        .with(withAuth("company")))
                .andExpect(status().isUnauthorized())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.UNAUTHORIZED, Constants.NO_RIGHTS)));
    }

    @Test
    public void setManagerByUserIdWhenCollision() throws Exception {
        assert mvc.perform(
                put(PATH + SET_MANAGER_BY_USER_ID, 1, 9)
                        .with(withAuth("companyWithSites")))
                .andExpect(status().isNotModified())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.MANAGER_COLLISION, Constants.MANAGER_ALREADY_SITE_MANAGER)));
    }

    @Test
    public void setManagerByUsernameShouldBeSuccess() throws Exception {
        assert mvc.perform(
                put(PATH + SET_MANAGER_BY_USERNAME, 1, "manager")
                        .with(withAuth("companyWithSites")))
                .andExpect(status().isAccepted())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new SuccessResponse()));
    }

    @Test
    public void setManagerByUsernameWhenInvalid() throws Exception {
        assert mvc.perform(
                put(PATH + SET_MANAGER_BY_USERNAME, 1, "invalidManager".toLowerCase())
                        .with(withAuth("companyWithSites")))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.NOT_FOUND, Constants.MANAGER_NOT_FOUND)));
    }

    @Test
    public void setManagerByUsernameWhenUnauthorized() throws Exception {
        assert mvc.perform(
                put(PATH + SET_MANAGER_BY_USERNAME, 1, "manager")
                        .with(withAuth("company")))
                .andExpect(status().isUnauthorized())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.UNAUTHORIZED, Constants.NO_RIGHTS)));
    }

    @Test
    public void setManagerByUsernameWhenCollision() throws Exception {
        assert mvc.perform(
                put(PATH + SET_MANAGER_BY_USERNAME, 1, "managerWithSite".toLowerCase())
                        .with(withAuth("companyWithSites")))
                .andExpect(status().isNotModified())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.MANAGER_COLLISION, Constants.MANAGER_ALREADY_SITE_MANAGER)));
    }

    @Test
    public void getByCompanyShouldBeSuccess() throws Exception {
        assert fromJsonList(
                mvc.perform(
                        get(PATH + GET_BY_COMPANY)
                                .with(withAuth("companyWithCars")))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString()
        ).size() == 2;
        assert fromJsonList(
                mvc.perform(
                        get(PATH + GET_BY_COMPANY)
                                .with(withAuth("companyWithCars")))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString()
        ).size() == 2;
    }

    @Test
    public void getByManagerShouldBeSuccess() throws Exception {
        assert mvc.perform(
                get(PATH + GET_BY_MANAGER)
                        .with(withAuth("managerWithSite")))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(siteService.getById(3)));
    }

    @Test
    public void getByManagerWhenNotManagedSite() throws Exception {
        assert mvc.perform(
                get(PATH + GET_BY_MANAGER)
                        .with(withAuth("manager")))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.NOT_FOUND, Constants.NO_MANAGED_SITE)));
    }

    @Test
    public void getByIdShouldBeSuccess() throws Exception {
        assert mvc.perform(
                get(PATH + GET_BY_ID, 1))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(siteService.getById(1)));
        assert mvc.perform(
                get(PATH + GET_BY_ID, 2))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(siteService.getById(2)));
        assert mvc.perform(
                get(PATH + GET_BY_ID, 3))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(siteService.getById(3)));
        assert mvc.perform(
                get(PATH + GET_BY_ID, 4))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(siteService.getById(4)));
    }

    @Test
    public void getByIdWhenInvalid() throws Exception {
        assert mvc.perform(
                get(PATH + GET_BY_ID, 100))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.NOT_FOUND, Constants.SITE_NOT_FOUND)));
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
        ).size() == 2;
        assert fromJsonList(
                mvc.perform(
                        get(PATH + GET_BY_COMPANY_NAME, "Company With Cars"))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString()
        ).size() == 2;
    }

    @Test
    public void getByCompanyNameWhenInvalid() throws Exception {
        assert mvc.perform(
                get(PATH + GET_BY_COMPANY_NAME, "Invalid Company"))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.NOT_FOUND, Constants.SITE_NOT_FOUND)));
    }

    private RequestPostProcessor withAuth(String username) {
        return httpBasic(username.toLowerCase(), "password");
    }

    private String toJson(Object object) throws JsonProcessingException {
        return objectMapper().writeValueAsString(object);
    }

    private List<SiteResponse> fromJsonList(String json) throws IOException {
        return objectMapper().readValue(json, objectMapper().getTypeFactory().constructCollectionType(List.class, SiteResponse.class));
    }

    @Bean
    private ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Autowired
    private SiteService siteService;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    private String PATH = Constants.PATH_SITE;
}