package hu.unideb.inf.carrental.reservation.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.unideb.inf.carrental.commons.constant.Constants;
import hu.unideb.inf.carrental.commons.exception.enumeration.ExceptionType;
import hu.unideb.inf.carrental.commons.model.CreatedResponse;
import hu.unideb.inf.carrental.commons.model.ErrorResponse;
import hu.unideb.inf.carrental.commons.model.SuccessResponse;
import hu.unideb.inf.carrental.reservation.resource.model.CreateReservationRequest;
import hu.unideb.inf.carrental.reservation.resource.model.ReservationResponse;
import hu.unideb.inf.carrental.reservation.service.ReservationService;
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
import java.time.LocalDate;
import java.util.List;

import static hu.unideb.inf.carrental.reservation.resource.ReservationResource.CLOSE;
import static hu.unideb.inf.carrental.reservation.resource.ReservationResource.RESERVE;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:beforeTestRun.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:afterTestRun.sql")
})
public class ReservationResourceTest {
    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    public void reserveShouldBeSuccessWhenCarId_1() throws Exception {
        CreateReservationRequest createReservationRequest = new CreateReservationRequest(1L, LocalDate.now().toString(), LocalDate.now().toString());

        assert mvc.perform(
                post(PATH + RESERVE)
                        .with(withAuth("customer"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(createReservationRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new CreatedResponse(5L)));
    }

    @Test
    public void reserveShouldBeSuccessWhenCarId_2() throws Exception {
        CreateReservationRequest createReservationRequest = new CreateReservationRequest(1L, LocalDate.now().plusDays(1L).toString(), LocalDate.now().plusDays(1L).toString());

        assert mvc.perform(
                post(PATH + RESERVE)
                        .with(withAuth("customer"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(createReservationRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new CreatedResponse(6L)));
    }

    @Test
    public void reserveShouldBeSuccessWhenCarId_3() throws Exception {
        CreateReservationRequest createReservationRequest = new CreateReservationRequest(3L, LocalDate.now().toString(), LocalDate.now().toString());

        assert mvc.perform(
                post(PATH + RESERVE)
                        .with(withAuth("customer"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(createReservationRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new CreatedResponse(7L)));
    }

    @Test
    public void reserveShouldBeSuccessWhenCarId_4() throws Exception {
        CreateReservationRequest createReservationRequest = new CreateReservationRequest(4L, LocalDate.now().toString(), LocalDate.now().toString());

        assert mvc.perform(
                post(PATH + RESERVE)
                        .with(withAuth("customer"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(createReservationRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new CreatedResponse(8L)));
    }


    @Test
    public void reserveWhenCarNotFound() throws Exception {
        CreateReservationRequest createReservationRequest = new CreateReservationRequest(100L, LocalDate.now().toString(), LocalDate.now().toString());

        assert mvc.perform(
                post(PATH + RESERVE)
                        .with(withAuth("customer"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(createReservationRequest)))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.NOT_FOUND, Constants.CAR_NOT_FOUND)));
    }

    @Test
    public void reserveWhenCarInRentWhenCarId_2() throws Exception {
        CreateReservationRequest createReservationRequest = new CreateReservationRequest(2L, LocalDate.now().toString(), LocalDate.now().toString());

        assert mvc.perform(
                post(PATH + RESERVE)
                        .with(withAuth("customer"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(createReservationRequest)))
                .andExpect(status().isNotModified())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.CAR_IN_RENT, Constants.CAR_ALREADY_IN_RENT)));
    }

    @Test
    public void reserveWhenCarInRentWhenCarId_4() throws Exception {
        CreateReservationRequest createReservationRequest = new CreateReservationRequest(4L, LocalDate.now().plusDays(1L).toString(), LocalDate.now().plusDays(1L).toString());

        assert mvc.perform(
                post(PATH + RESERVE)
                        .with(withAuth("customer"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(createReservationRequest)))
                .andExpect(status().isNotModified())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.CAR_IN_RENT, Constants.CAR_ALREADY_IN_RENT)));
    }

    @Test
    public void reserveWhenCollision() throws Exception {
        CreateReservationRequest createReservationRequest = new CreateReservationRequest(1L, LocalDate.now().toString(), LocalDate.now().toString());

        assert mvc.perform(
                post(PATH + RESERVE)
                        .with(withAuth("customerActiveReservation"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(createReservationRequest)))
                .andExpect(status().isNotModified())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.RESERVATION_COLLISION, Constants.CUSTOMER_HAS_RESERVATION)));
    }

    @Test
    public void reserveWhenInputInvalid() throws Exception {
        CreateReservationRequest createReservationRequest = new CreateReservationRequest(1L, LocalDate.now().minusDays(1L).toString(), LocalDate.now().plusDays(1L).toString());

        assert mvc.perform(
                post(PATH + RESERVE)
                        .with(withAuth("customer"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(createReservationRequest)))
                .andExpect(status().isNotModified())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.ILLEGAL_ARGUMENT, Constants.INVALID_DATE)));
    }

    @Test
    public void closeShouldBeSuccess() throws Exception {
        assert mvc.perform(
                put(PATH + CLOSE, 1)
                        .with(withAuth("companyWithCars")))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new SuccessResponse()));

        assert mvc.perform(
                put(PATH + CLOSE, 4)
                        .with(withAuth("companyWithCars")))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new SuccessResponse()));
    }

    @Test
    public void closeWhenUnauthorized() throws Exception {
        assert mvc.perform(
                put(PATH + CLOSE, 1)
                        .with(withAuth("company")))
                .andExpect(status().isUnauthorized())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.UNAUTHORIZED, Constants.NO_RIGHTS)));
    }

    @Test
    public void closeWhenNotFound() throws Exception {
        assert mvc.perform(
                put(PATH + CLOSE, 100)
                        .with(withAuth("company")))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString()
                .equals(toJson(new ErrorResponse(ExceptionType.NOT_FOUND, Constants.RESERVATION_NOT_FOUND)));
    }



    private RequestPostProcessor withAuth(String username) {
        return httpBasic(username.toLowerCase(), "password");
    }

    private String toJson(Object object) throws JsonProcessingException {
        return objectMapper().writeValueAsString(object);
    }

    private List<ReservationResponse> fromJsonList(String json) throws IOException {
        return objectMapper().readValue(json, objectMapper().getTypeFactory().constructCollectionType(List.class, ReservationResponse.class));
    }

    @Bean
    private ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    private String PATH = Constants.PATH_RESERVATION;
}