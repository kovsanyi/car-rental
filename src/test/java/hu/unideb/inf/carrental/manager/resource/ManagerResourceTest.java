package hu.unideb.inf.carrental.manager.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.unideb.inf.carrental.commons.constant.Constants;
import hu.unideb.inf.carrental.commons.domain.manager.Manager;
import hu.unideb.inf.carrental.commons.domain.user.User;
import hu.unideb.inf.carrental.commons.domain.user.enumeration.UserRole;
import hu.unideb.inf.carrental.commons.exception.enumeration.ExceptionType;
import hu.unideb.inf.carrental.commons.model.CreatedResponse;
import hu.unideb.inf.carrental.commons.model.ErrorResponse;
import hu.unideb.inf.carrental.commons.model.SuccessResponse;
import hu.unideb.inf.carrental.manager.resource.model.CreateManagerRequest;
import hu.unideb.inf.carrental.manager.resource.model.UpdateManagerRequest;
import hu.unideb.inf.carrental.manager.service.ManagerService;
import hu.unideb.inf.carrental.manager.service.converter.CreateManagerRequestConverter;
import hu.unideb.inf.carrental.manager.service.converter.ManagerResponseConverter;
import hu.unideb.inf.carrental.manager.service.converter.UpdateManagerRequestConverter;
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
public class ManagerResourceTest {

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        managerService.save(testCreateManagerRequest());
    }

    @Test
    public void saveShouldBeSuccess() throws Exception {
        CreateManagerRequest request = testCreateManagerRequest();
        request.setUserUsername("test2");
        request.setUserEmail("test2@mail.com");

        assert mvc.perform(
                post(Constants.PATH_MANAGER + ManagerResource.SAVE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new CreatedResponse(2L)));
    }

    @Test
    public void saveWhenUsernameAlreadyExists() throws Exception {
        CreateManagerRequest request = testCreateManagerRequest();
        request.setUserEmail("test2@mail.com");

        assert mvc.perform(
                post(Constants.PATH_MANAGER + ManagerResource.SAVE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andExpect(status().isNotAcceptable())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.USERNAME_EXISTS, Constants.USERNAME_ALREADY_EXISTS)));
    }

    @Test
    public void saveWhenEmailAlreadyExists() throws Exception {
        CreateManagerRequest request = testCreateManagerRequest();
        request.setUserUsername("test2");

        assert mvc.perform(
                post(Constants.PATH_MANAGER + ManagerResource.SAVE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andExpect(status().isNotAcceptable())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.EMAIL_EXISTS, Constants.EMAIL_ALREADY_EXISTS)));
    }

    @Test
    public void updateShouldBeSuccess() throws Exception {
        UpdateManagerRequest request = testUpdateManagerRequest();

        assert mvc.perform(
                put(Constants.PATH_MANAGER + ManagerResource.UPDATE)
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
                delete(Constants.PATH_MANAGER + ManagerResource.DELETE)
                        .with(httpBasic(testUser().getUsername(), testUser().getPassword())))
                .andExpect(status().isAccepted())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new SuccessResponse()));
    }

    @Test
    public void getShouldBeSuccess() throws Exception {
        assert mvc.perform(
                get(Constants.PATH_MANAGER + ManagerResource.GET_ROOT)
                        .with(httpBasic(testUser().getUsername(), testUser().getPassword())))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(managerResponseConverter.from(testManager())));
    }

    @Test
    public void getByIdShouldBeSuccess() throws Exception {
        assert mvc.perform(
                get(Constants.PATH_MANAGER + ManagerResource.GET_BY_ID, testManager().getId())
                        .with(httpBasic(testUser().getUsername(), testUser().getPassword())))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(managerResponseConverter.from(testManager())));
    }

    @Test
    public void getByIdWhenInputInvalid() throws Exception {
        assert mvc.perform(
                get(Constants.PATH_MANAGER + ManagerResource.GET_BY_ID, 2)
                        .with(httpBasic(testUser().getUsername(), testUser().getPassword())))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.NOT_FOUND, Constants.MANAGER_NOT_FOUND)));
    }

    @Test
    public void getByUserIdShouldBeSuccess() throws Exception {
        assert mvc.perform(
                get(Constants.PATH_MANAGER + ManagerResource.GET_BY_USER_ID, testUser().getId())
                        .with(httpBasic(testUser().getUsername(), testUser().getPassword())))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(managerResponseConverter.from(testManager())));
    }

    @Test
    public void getByUserIdWhenInputInvalid() throws Exception {
        assert mvc.perform(
                get(Constants.PATH_MANAGER + ManagerResource.GET_BY_USER_ID, 2)
                        .with(httpBasic(testUser().getUsername(), testUser().getPassword())))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.NOT_FOUND, Constants.MANAGER_NOT_FOUND)));
    }

    @Test
    public void getByUsernameShouldBeSuccess() throws Exception {
        assert mvc.perform(
                get(Constants.PATH_MANAGER + ManagerResource.GET_BY_USERNAME, testUser().getUsername())
                        .with(httpBasic(testUser().getUsername(), testUser().getPassword())))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(managerResponseConverter.from(testManager())));
    }

    @Test
    public void getByUsernameWhenInputInvalid() throws Exception {
        assert mvc.perform(
                get(Constants.PATH_MANAGER + ManagerResource.GET_BY_USERNAME, "invalidUsername")
                        .with(httpBasic(testUser().getUsername(), testUser().getPassword())))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.NOT_FOUND, Constants.MANAGER_NOT_FOUND)));
    }

    private UpdateManagerRequest testUpdateManagerRequest() {
        UpdateManagerRequest request = new UpdateManagerRequest();
        request.setFullName(testManager().getFullName() + "updated");
        request.setPhoneNumber(testManager().getPhoneNumber());
        request.setZipCode(testManager().getZipCode());
        request.setCity(testManager().getCity());
        request.setAddress(testManager().getAddress());
        return request;
    }

    private CreateManagerRequest testCreateManagerRequest() {
        CreateManagerRequest request = new CreateManagerRequest();
        request.setUserUsername(testManager().getUser().getUsername());
        request.setUserPassword(testManager().getUser().getPassword());
        request.setUserEmail(testManager().getUser().getEmail());
        request.setFullName(testManager().getFullName());
        request.setPhoneNumber(testManager().getPhoneNumber());
        request.setZipCode(testManager().getZipCode());
        request.setCity(testManager().getCity());
        request.setAddress(testManager().getAddress());
        return request;
    }

    private Manager testManager() {
        Manager manager = new Manager();
        manager.setId(1L);
        manager.setUser(testUser());
        manager.setFullName("Test");
        manager.setPhoneNumber("123");
        manager.setZipCode(1234);
        manager.setCity("City");
        manager.setAddress("Address");
        return manager;
    }

    private User testUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setPassword("password");
        user.setEmail("test@mail.com");
        user.setRole(UserRole.ROLE_MANAGER);
        return user;
    }

    private String toJson(Object object) throws JsonProcessingException {
        return objectMapper().writeValueAsString(object);
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(ManagerResourceTest.class);

    @Autowired
    ManagerService managerService;

    @Autowired
    CreateManagerRequestConverter createManagerRequestConverter;

    @Autowired
    UpdateManagerRequestConverter updateManagerRequestConverter;

    @Autowired
    ManagerResponseConverter managerResponseConverter;

    @Autowired
    WebApplicationContext context;

    private MockMvc mvc;

    @Bean
    private ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
