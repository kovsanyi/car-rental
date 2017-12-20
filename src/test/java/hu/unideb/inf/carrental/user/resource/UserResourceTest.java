package hu.unideb.inf.carrental.user.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.unideb.inf.carrental.commons.constant.Constants;
import hu.unideb.inf.carrental.commons.domain.user.User;
import hu.unideb.inf.carrental.commons.domain.user.enumeration.UserRole;
import hu.unideb.inf.carrental.commons.exception.enumeration.ExceptionType;
import hu.unideb.inf.carrental.commons.model.ErrorResponse;
import hu.unideb.inf.carrental.user.service.UserService;
import hu.unideb.inf.carrental.user.service.converter.UserResponseConverter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UserResourceTest {

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        userService.save(testUser());
    }

    @Test
    public void getShouldBeSuccess() throws Exception {
        assert mvc.perform(MockMvcRequestBuilders
                .get(Constants.PATH_USER + UserResource.GET_ROOT)
                .with(httpBasic(testUser().getUsername(), testUser().getPassword())))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(userResponseConverter.from(testUser())));
    }

    @Test
    public void getByIdWhenInputValid() throws Exception {
        assert mvc.perform(MockMvcRequestBuilders
                .get(Constants.PATH_USER + UserResource.GET_BY_ID, 1)
                .with(httpBasic(testUser().getUsername(), testUser().getPassword())))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(userResponseConverter.from(testUser())));
    }

    @Test
    public void getByIdWhenInputInvalid() throws Exception {
        assert mvc.perform(MockMvcRequestBuilders
                .get(Constants.PATH_USER + UserResource.GET_BY_ID, 2)
                .with(httpBasic(testUser().getUsername(), testUser().getPassword())))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.NOT_FOUND, Constants.USER_NOT_FOUND)));
    }

    @Test
    public void getByUsernameWhenInputValid() throws Exception {
        assert mvc.perform(MockMvcRequestBuilders
                .get(Constants.PATH_USER + UserResource.GET_BY_USERNAME, testUser().getUsername())
                .with(httpBasic(testUser().getUsername(), testUser().getPassword())))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(userResponseConverter.from(testUser())));
    }

    @Test
    public void getByUsernameWhenInputInvalid() throws Exception {
        assert mvc.perform(MockMvcRequestBuilders
                .get(Constants.PATH_USER + UserResource.GET_BY_USERNAME, "invalidUsername")
                .with(httpBasic(testUser().getUsername(), testUser().getPassword())))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.NOT_FOUND, Constants.USER_NOT_FOUND)));

    }

    private User testUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setPassword("test");
        user.setEmail("test@mail.com");
        user.setRole(UserRole.ROLE_COMPANY);
        return user;
    }

    private String toJson(Object object) throws JsonProcessingException {
        return objectMapper().writeValueAsString(object);
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(UserResourceTest.class);

    @Autowired
    UserService userService;

    @Autowired
    UserResponseConverter userResponseConverter;

    @Autowired
    WebApplicationContext context;

    private MockMvc mvc;

    @Bean
    private ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
