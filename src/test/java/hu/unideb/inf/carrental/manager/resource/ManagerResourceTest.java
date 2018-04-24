package hu.unideb.inf.carrental.manager.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.unideb.inf.carrental.commons.constant.Constants;
import hu.unideb.inf.carrental.commons.exception.enumeration.ExceptionType;
import hu.unideb.inf.carrental.commons.model.CreatedResponse;
import hu.unideb.inf.carrental.commons.model.ErrorResponse;
import hu.unideb.inf.carrental.commons.model.SuccessResponse;
import hu.unideb.inf.carrental.manager.resource.model.CreateManagerRequest;
import hu.unideb.inf.carrental.manager.resource.model.ManagerResponse;
import hu.unideb.inf.carrental.manager.resource.model.UpdateManagerRequest;
import hu.unideb.inf.carrental.manager.service.ManagerService;
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

import static hu.unideb.inf.carrental.manager.resource.ManagerResource.*;
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
public class ManagerResourceTest {

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    public void saveShouldBeSuccess() throws Exception {
        CreateManagerRequest createManagerRequest = new CreateManagerRequest("newManager".toLowerCase(), "password", "newmanager@mail.com", "New Manager", "11111111111", 1111, "City", "Address");
        ManagerResponse managerResponse = new ManagerResponse(3L, 10L, "New Manager", "11111111111", 1111, "City", "Address");

        assert mvc.perform(
                post(PATH + SAVE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(createManagerRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new CreatedResponse(3L)));

        assert mvc.perform(
                get(PATH + GET_BY_ID, 3))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(managerResponse));
    }

    @Test
    public void saveWhenUsernameAlreadyExists() throws Exception {
        CreateManagerRequest createManagerRequest = new CreateManagerRequest("manager".toLowerCase(), "password", "newmanager@mail.com", "New Manager", "11111111111", 1111, "City", "Address");

        assert mvc.perform(
                post(PATH + SAVE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(createManagerRequest)))
                .andExpect(status().isNotAcceptable())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.USERNAME_EXISTS, Constants.USERNAME_ALREADY_EXISTS)));
    }

    @Test
    public void saveWhenEmailAlreadyExists() throws Exception {
        CreateManagerRequest createManagerRequest = new CreateManagerRequest("newManager".toLowerCase(), "password", "manager@mail.com", "New Manager", "11111111111", 1111, "City", "Address");

        assert mvc.perform(
                post(PATH + SAVE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(createManagerRequest)))
                .andExpect(status().isNotAcceptable())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.EMAIL_EXISTS, Constants.EMAIL_ALREADY_EXISTS)));
    }

    @Test
    public void updateShouldBeSuccess() throws Exception {
        UpdateManagerRequest updateManagerRequest = new UpdateManagerRequest("Updated Manager", "11111111112", 1112, "City1", "Address1");
        ManagerResponse managerResponse = new ManagerResponse(1L, 8L, "Updated Manager", "11111111112", 1112, "City1", "Address1");

        assert mvc.perform(
                put(PATH + UPDATE)
                        .with(withAuth("manager"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(updateManagerRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new SuccessResponse()));

        assert mvc.perform(
                get(PATH + GET_BY_ID, 1))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(managerResponse));
    }

    @Test
    public void deleteShouldBeSuccess() throws Exception {
        assert mvc.perform(
                delete(PATH + DELETE)
                        .with(withAuth("manager")))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new SuccessResponse()));

        assert mvc.perform(
                delete(PATH + DELETE)
                        .with(withAuth("managerWithSite")))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new SuccessResponse()));
    }

    @Test
    public void deleteWhenAlreadyDeleted() throws Exception {
        assert mvc.perform(
                delete(PATH + DELETE)
                        .with(withAuth("manager")))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new SuccessResponse()));

        mvc.perform(
                delete(PATH + DELETE)
                        .with(withAuth("manager")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void getShouldBeSuccess() throws Exception {
        assert mvc.perform(
                get(PATH + GET_ROOT)
                        .with(withAuth("manager")))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(managerService.getById(1L)));

        assert mvc.perform(
                get(PATH + GET_ROOT)
                        .with(withAuth("managerWithSite")))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(managerService.getById(2L)));
    }

    @Test
    public void getByIdShouldBeSuccess() throws Exception {
        assert mvc.perform(
                get(PATH + GET_BY_ID, 1))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(managerService.getById(1L)));

        assert mvc.perform(
                get(PATH + GET_BY_ID, 2))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(managerService.getById(2L)));
    }

    @Test
    public void getByIdWhenInputInvalid() throws Exception {
        assert mvc.perform(
                get(PATH + GET_BY_ID, 100))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.NOT_FOUND, Constants.MANAGER_NOT_FOUND)));
    }

    @Test
    public void getByUserIdShouldBeSuccess() throws Exception {
        assert mvc.perform(
                get(PATH + GET_BY_USER_ID, 8))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(managerService.getById(1L)));

        assert mvc.perform(
                get(PATH + GET_BY_USER_ID, 9))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(managerService.getById(2L)));
    }

    @Test
    public void getByUserIdWhenInputInvalid() throws Exception {
        assert mvc.perform(
                get(PATH + GET_BY_USER_ID, 1))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.NOT_FOUND, Constants.MANAGER_NOT_FOUND)));
    }

    @Test
    public void getByUsernameShouldBeSuccess() throws Exception {
        assert mvc.perform(
                get(PATH + GET_BY_USERNAME, "manager"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(managerService.getById(1L)));

        assert mvc.perform(
                get(PATH + GET_BY_USERNAME, "managerWithSite"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(managerService.getById(2L)));

    }

    @Test
    public void getByUsernameWhenInputInvalid() throws Exception {
        assert mvc.perform(
                get(PATH + GET_BY_USERNAME, "company"))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.NOT_FOUND, Constants.MANAGER_NOT_FOUND)));
    }

    private RequestPostProcessor withAuth(String username) {
        return httpBasic(username.toLowerCase(), "password");
    }

    private String toJson(Object object) throws JsonProcessingException {
        return objectMapper().writeValueAsString(object);
    }

    @Bean
    private ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Autowired
    private ManagerService managerService;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    private String PATH = Constants.PATH_MANAGER;
}
