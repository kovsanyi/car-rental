package hu.unideb.inf.carrental.user.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.unideb.inf.carrental.commons.constant.Constants;
import hu.unideb.inf.carrental.commons.exception.enumeration.ExceptionType;
import hu.unideb.inf.carrental.commons.model.ErrorResponse;
import hu.unideb.inf.carrental.user.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static hu.unideb.inf.carrental.user.resource.UserResource.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:beforeTestRun.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:afterTestRun.sql")
})
public class UserResourceTest {

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    public void getShouldBeSuccess() throws Exception {
        assert mvc.perform(
                get(PATH + GET_ROOT)
                        .with(withAuth("company")))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(userService.getById(1L)));

        assert mvc.perform(
                get(PATH + GET_ROOT)
                        .with(withAuth("customer")))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(userService.getById(4L)));

        assert mvc.perform(
                get(PATH + GET_ROOT)
                        .with(withAuth("manager")))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(userService.getById(8L)));
    }

    @Test
    public void getByIdWhenInputValid() throws Exception {
        assert mvc.perform(
                get(PATH + GET_BY_ID, 1)
                        .with(withAuth("company")))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(userService.getById(1L)));

        assert mvc.perform(
                get(PATH + GET_BY_ID, 4)
                        .with(withAuth("company")))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(userService.getById(4L)));

        assert mvc.perform(
                get(PATH + GET_BY_ID, 8)
                        .with(withAuth("company")))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(userService.getById(8L)));
    }

    @Test
    public void getByIdWhenInputInvalid() throws Exception {
        assert mvc.perform(
                get(PATH + GET_BY_ID, 100)
                        .with(withAuth("company")))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.NOT_FOUND, Constants.USER_NOT_FOUND)));
    }

    @Test
    public void getByUsernameShouldBeSuccess() throws Exception {
        assert mvc.perform(
                get(PATH + GET_BY_USERNAME, "company")
                        .with(withAuth("company")))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(userService.getById(1L)));

        assert mvc.perform(
                get(PATH + GET_BY_USERNAME, "customer")
                        .with(withAuth("company")))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(userService.getById(4L)));

        assert mvc.perform(
                get(PATH + GET_BY_USERNAME, "manager")
                        .with(withAuth("company")))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(userService.getById(8L)));
    }

    @Test
    public void getByUsernameWhenInputInvalid() throws Exception {
        assert mvc.perform(
                get(PATH + GET_BY_USERNAME, "invalidUsername")
                        .with(withAuth("company")))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.NOT_FOUND, Constants.USER_NOT_FOUND)));
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
    private UserService userService;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    private String PATH = Constants.PATH_USER;
}
