package hu.unideb.inf.carrental.reservation.service;

import hu.unideb.inf.carrental.commons.exception.*;
import hu.unideb.inf.carrental.reservation.resource.model.CreateReservationRequest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:beforeTestRun.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:afterTestRun.sql")
})
public class ReservationServiceTest {
    @Test
    public void reserveShouldBeSuccessWhenCarId_1() throws Exception {
        setAuth("customer");
        CreateReservationRequest createReservationRequest = new CreateReservationRequest(1L, LocalDate.now().toString(), LocalDate.now().toString());
        reservationService.reserve(createReservationRequest);
    }

    @Test
    public void reserveShouldBeSuccessWhenCarId_2() throws Exception {
        setAuth("customer");
        CreateReservationRequest createReservationRequest = new CreateReservationRequest(1L, LocalDate.now().plusDays(1L).toString(), LocalDate.now().plusDays(1L).toString());
        reservationService.reserve(createReservationRequest);
    }

    @Test
    public void reserveShouldBeSuccessWhenCarId_3() throws Exception {
        setAuth("customer");
        CreateReservationRequest createReservationRequest = new CreateReservationRequest(3L, LocalDate.now().toString(), LocalDate.now().toString());
        reservationService.reserve(createReservationRequest);
    }

    @Test
    public void reserveShouldBeSuccessWhenCarId_4() throws Exception {
        setAuth("customer");
        CreateReservationRequest createReservationRequest = new CreateReservationRequest(4L, LocalDate.now().toString(), LocalDate.now().toString());
        reservationService.reserve(createReservationRequest);
    }

    @Test(expected = NotFoundException.class)
    public void reserveShouldThrowNotFoundException() throws Exception {
        setAuth("customer");
        CreateReservationRequest createReservationRequest = new CreateReservationRequest(100L, LocalDate.now().toString(), LocalDate.now().toString());
        reservationService.reserve(createReservationRequest);
    }

    @Test(expected = CarInRentException.class)
    public void reserveShouldThrowCarInRentExceptionWhenCarId_2() throws Exception {
        setAuth("customer");
        CreateReservationRequest createReservationRequest = new CreateReservationRequest(2L, LocalDate.now().toString(), LocalDate.now().toString());
        reservationService.reserve(createReservationRequest);
    }

    @Test(expected = CarInRentException.class)
    public void reserveShouldThrowCarInRentExceptionWhenCarId_4() throws Exception {
        setAuth("customer");
        CreateReservationRequest createReservationRequest = new CreateReservationRequest(4L, LocalDate.now().plusDays(1L).toString(), LocalDate.now().plusDays(1L).toString());
        reservationService.reserve(createReservationRequest);
    }

    @Test(expected = ReservationCollisionException.class)
    public void reserveShouldThrowReservationCollisionException() throws Exception {
        setAuth("customerActiveReservation");
        CreateReservationRequest createReservationRequest = new CreateReservationRequest(1L, LocalDate.now().toString(), LocalDate.now().toString());
        reservationService.reserve(createReservationRequest);
    }

    @Test(expected = InvalidInputException.class)
    public void reserveShouldThrowInvalidInputException() throws Exception {
        setAuth("customer");
        CreateReservationRequest createReservationRequest = new CreateReservationRequest(1L, LocalDate.now().minusDays(1L).toString(), LocalDate.now().plusDays(1L).toString());
        reservationService.reserve(createReservationRequest);
    }

    @Test
    public void closeShouldBeSuccess() throws Exception {
        setAuth("companyWithCars");
        reservationService.close(1L);
        reservationService.close(4L);

        setAuth("customerActiveReservation");
        assert reservationService.getClosedByCustomer().size() == 1;
        setAuth("customerReservation");
        assert reservationService.getClosedByCustomer().size() == 2;
    }

    @Test(expected = UnauthorizedAccessException.class)
    public void closeShouldThrowUnauthorizedAccessException() throws Exception {
        setAuth("company");
        reservationService.close(1L);
    }

    @Test(expected = NotFoundException.class)
    public void closeShouldThrowNotFoundException() throws Exception {
        setAuth("company");
        reservationService.close(100L);
    }

    @Test
    public void getActiveByCustomerShouldBeSuccess() throws Exception {
        setAuth("customerActiveReservation");
        assert reservationService.getActiveByCustomer().getId().equals(1L);
        setAuth("customerReservation");
        assert reservationService.getActiveByCustomer().getId().equals(4L);
    }

    @Test(expected = NotFoundException.class)
    public void getActiveByCustomerShouldThrowNotFoundExceptionWhenCustomerId_1() throws Exception {
        setAuth("customer");
        reservationService.getActiveByCustomer();
    }

    @Test(expected = NotFoundException.class)
    public void getActiveByCustomerShouldThrowNotFoundExceptionWhenCustomerId_4() throws Exception {
        setAuth("customerClosedReservation");
        reservationService.getActiveByCustomer();
    }

    @Test
    public void getClosedByCustomerShouldBeSuccess() {
        setAuth("customer");
        assert reservationService.getClosedByCustomer().isEmpty();
        setAuth("customerActiveReservation");
        assert reservationService.getClosedByCustomer().isEmpty();
        setAuth("customerClosedReservation");
        assert reservationService.getClosedByCustomer().size() == 1;
        assert reservationService.getClosedByCustomer().get(0).getId().equals(2L);
        setAuth("customerReservation");
        assert reservationService.getClosedByCustomer().size() == 1;
        assert reservationService.getClosedByCustomer().get(0).getId().equals(3L);
    }

    @Test
    public void getAllByCustomerShouldBeSuccess() {
        setAuth("customer");
        assert reservationService.getAllByCustomer().isEmpty();
        setAuth("customerActiveReservation");
        assert reservationService.getAllByCustomer().size() == 1;
        assert reservationService.getAllByCustomer().get(0).getId().equals(1L);
        setAuth("customerClosedReservation");
        assert reservationService.getAllByCustomer().size() == 1;
        assert reservationService.getAllByCustomer().get(0).getId().equals(2L);
        setAuth("customerReservation");
        assert reservationService.getAllByCustomer().size() == 2;
    }

    @Test
    public void getActiveBySiteIdShouldBeSuccess() throws Exception {
        setAuth("companyWithSites");
        assert reservationService.getActiveBySiteId(1L).size() == 0;
        assert reservationService.getActiveBySiteId(2L).size() == 0;
        setAuth("companyWithCars");
        assert reservationService.getActiveBySiteId(3L).size() == 1;
        assert reservationService.getActiveBySiteId(4L).size() == 1;
        assert reservationService.getActiveBySiteId(4L).get(0).getId().equals(4L);
    }

    @Test(expected = NotFoundException.class)
    public void getActiveBySiteIdShouldThrowNotFoundException() throws Exception {
        setAuth("company");
        reservationService.getActiveBySiteId(100L);
    }

    @Test(expected = UnauthorizedAccessException.class)
    public void getActiveBySiteIdShouldThrowUnauthorizedAccessException() throws Exception {
        setAuth("company");
        reservationService.getActiveBySiteId(1L);
    }

    @Test
    public void getClosedBySiteIdShouldBeSuccess() throws Exception {
        setAuth("companyWithSites");
        assert reservationService.getClosedBySiteId(1L).size() == 0;
        assert reservationService.getClosedBySiteId(2L).size() == 0;
        setAuth("companyWithCars");
        assert reservationService.getClosedBySiteId(3L).size() == 0;
        assert reservationService.getClosedBySiteId(4L).size() == 2;
    }

    @Test(expected = NotFoundException.class)
    public void getClosedBySiteIdShouldThrowNotFoundException() throws Exception {
        setAuth("company");
        reservationService.getClosedBySiteId(100L);
    }

    @Test(expected = UnauthorizedAccessException.class)
    public void getClosedBySiteIdShouldThrowUnauthorizedAccessException() throws Exception {
        setAuth("company");
        reservationService.getClosedBySiteId(1L);
    }

    @Test
    public void getAllBySiteIdShouldBeSuccess() throws Exception {
        setAuth("companyWithSites");
        assert reservationService.getAllBySiteId(1L).size() == 0;
        assert reservationService.getAllBySiteId(2L).size() == 0;
        setAuth("companyWithCars");
        assert reservationService.getAllBySiteId(3L).size() == 1;
        assert reservationService.getAllBySiteId(4L).size() == 3;
    }

    @Test(expected = NotFoundException.class)
    public void getAllBySiteIdShouldThrowNotFoundException() throws Exception {
        setAuth("company");
        reservationService.getAllBySiteId(100L);
    }


    @Test(expected = UnauthorizedAccessException.class)
    public void getAllBySiteIdShouldThrowUnauthorizedAccessException() throws Exception {
        setAuth("company");
        reservationService.getAllBySiteId(1L);
    }

    @Test
    public void getActiveByCompanyShouldBeSuccess() {
        setAuth("company");
        assert reservationService.getActiveByCompany().size() == 0;
        setAuth("companyWithSites");
        assert reservationService.getActiveByCompany().size() == 0;
        setAuth("companyWithCars");
        assert reservationService.getActiveByCompany().size() == 2;
    }

    @Test
    public void getClosedByCompanyShouldBeSuccess() {
        setAuth("company");
        assert reservationService.getClosedByCompany().size() == 0;
        setAuth("companyWithSites");
        assert reservationService.getClosedByCompany().size() == 0;
        setAuth("companyWithCars");
        assert reservationService.getClosedByCompany().size() == 2;
    }

    @Test
    public void getAllByCompanyShouldBeSuccess() {
        setAuth("company");
        assert reservationService.getAllByCompany().size() == 0;
        setAuth("companyWithSites");
        assert reservationService.getAllByCompany().size() == 0;
        setAuth("companyWithCars");
        assert reservationService.getAllByCompany().size() == 4;
    }

    private void setAuth(String username) {
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(username.toLowerCase(), "password");
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private AuthenticationManager authenticationManager;
}